
package com.challenge.university.controllers;

import com.challenge.university.models.dao.IUserDao;
import com.challenge.university.models.entity.Matter;
import com.challenge.university.models.entity.Teacher;
import com.challenge.university.models.entity.Usuario;
import com.challenge.university.models.services.IMatterService;
import com.challenge.university.models.services.ITeacherService;
import com.challenge.university.models.services.IUserService;
import com.challenge.university.utils.RenderPage;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/")
public class MatterController {
        
    @Autowired
    private IMatterService iMatterService;

    @Autowired
    private IUserService iUserService;
         
    @Autowired
    private ITeacherService iTeacherService;
    @Autowired
    private IUserDao iUserDao;
           
    private boolean inscripcion = false;
   
    private Matter m = null;
    
    @GetMapping(path="/") 
    public String index(Model model){
        return "index";
        
    }

    @GetMapping(path="inscriptions") 
    public String inscriptions(@RequestParam(name="page", defaultValue = "0") int page, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
        String name = auth.getName();
        Usuario u = iUserDao.findByUsername(name);
        Pageable matterPegeable = PageRequest.of(page, 6);
        Page<Matter> matterPag = iMatterService.getAll(matterPegeable);
        RenderPage<Matter> renderPag = new RenderPage<Matter>("/inscriptions", matterPag);
        List<Matter> mats = iMatterService.findAll();
        List<Long> inscripciones = new ArrayList();
        for(Matter m : mats){
            for(Usuario student : m.getStudents()){
                if(student.getId_user()== u.getId_user()){
                    inscripciones.add(m.getId_matter());
                }
            }
        }
        
        
        model.addAttribute("inscripciones", inscripciones);
        model.addAttribute("page", renderPag);
        model.addAttribute("matters", matterPag);

        return "inscriptions";
   
        
    }

    
    @GetMapping("teacher/{id_matter}") 
    public String teachers(Model model, Matter matter){
        List<Teacher> teachers = iTeacherService.findAll();
        m = iMatterService.findById(matter.getId_matter());
        model.addAttribute("teachers", teachers);
        //model.addAttribute("matter", m);
        return "teachers";
        
    }
    @GetMapping("description/{id_matter}") 
    public String description(Model model, Matter matter){
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
        String name = auth.getName();
        Usuario u = iUserDao.findByUsername(name);
        m = iMatterService.findById(matter.getId_matter());
        

        
        for(Usuario student : m.getStudents()){
            if(student.getId_user()== u.getId_user()){
                inscripcion = true;
            }
        }
        
        model.addAttribute("inscripcion", inscripcion);
        model.addAttribute("matter", m);
        //model.addAttribute("matter", m);
        return "description";
        
    }
    @GetMapping("formCreate") 
    public String formCreate(Matter matter){
        return "create";
        
    }
    @GetMapping("teacher/formCreateTeacher") 
    public String formCreate(Teacher teacher){
        return "createTeacher";
        
    }
    @GetMapping("delete/{id_matter}") 
    public String delete(Matter matter, Model model){
        iMatterService.delete(matter.getId_matter());
        //model.addAttribute(matter);
        return "redirect:/inscriptions";
        
    }
    @GetMapping("edit/{id_matter}") 
    public String edit(Matter matter, Model model){
        matter = iMatterService.findById(matter.getId_matter());
        model.addAttribute("matter", matter);
        return "edit";
        
    }

    @PostMapping("create")
    public String create(@Valid Matter matter, BindingResult result, Model model){
        if(result.hasErrors()){
            System.out.println("Hubo errores en el formulario");
             return "create";
        }
        iMatterService.save(matter);
        
        return "redirect:/inscriptions";

        
    }
    @PostMapping("teacher/createTeacher")
    public String create(@Valid Teacher teacher, BindingResult result){
        if(result.hasErrors()){
            System.out.println("Hubo errores en el formulario");
             return "createTeacher";
        }
        teacher.setActive(false);
        iTeacherService.save(teacher);
        return "redirect:/inscriptions";

        
    }
    @PostMapping("edit/modificar/{id_matter}")
    public String modificar(@Valid Matter matter, BindingResult result, Model model){
        if(result.hasErrors()){
            matter = iMatterService.findById(matter.getId_matter());
            model.addAttribute(matter);
            System.out.println("Hubo errores en el formulario");
             return "edit";
        }
        Matter matterActual = iMatterService.findById(matter.getId_matter());
        Matter matterUpdated = null;
        Teacher teacher = null;
        Teacher teacherUpdated = null;
                    
        matterActual.setName(matter.getName());
        matterActual.setSchedule(matter.getSchedule());
        matterActual.setDescription(matter.getDescription());
        matterActual.setQuota(matter.getQuota());
        if(matter.getTeacher()!= null){
            teacher = matterActual.getTeacher();
            teacherUpdated = iTeacherService.findById(teacher.getId_teacher());
            teacherUpdated.setActive(false);
            iTeacherService.save(teacherUpdated);
            
        }
        matterActual.setTeacher(matter.getTeacher());
        matterUpdated = iMatterService.save(matterActual);
      
        return "redirect:/inscriptions";
    }
    @PostMapping("teacher/add/{id_teacher}")
    public String addTeacher(Teacher teacher){
        Matter matterActual = iMatterService.findById(m.getId_matter());
        
        Matter matterUpdated = null;
        Teacher teacherUpdated = iTeacherService.findById(teacher.getId_teacher());
        Teacher teacherUpdated1 = null;
        if(teacher.isActive()){
           return "redirect:/inscriptions"; 
        }
        else{
            teacherUpdated.setActive(true);
            teacherUpdated1 = iTeacherService.save(teacherUpdated);
            matterActual.setTeacher(iTeacherService.findById(teacherUpdated1.getId_teacher()));

            matterUpdated = iMatterService.save(matterActual);

            return "redirect:/inscriptions"; 
        }     
        
    }
    @PostMapping("description/add")
    public String addStudent(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
        String name = auth.getName();
        Usuario u = iUserDao.findByUsername(name);
        Matter matterActual = iMatterService.findById(m.getId_matter());       
        Matter matterUpdated = null;
        Usuario studentUpdated = iUserService.findById(u.getId_user());
        Usuario studentUpdated1 = null;
        
        try{
            
            if(matterActual.getQuota()== matterActual.getStudents().size()){
                //retornaremos un mensaje de cupo lleno
                return "404"; 
            }
            else{ 
                matterActual.addStudent(iUserService.findById(studentUpdated.getId_user()));
                //studentUpdated1 = iUserService.save(studentUpdated);
                matterUpdated = iMatterService.save(matterActual);
                return "redirect:/inscriptions";
            }
           
        }catch(DataAccessException e){
            return "error";
        }


        }  

}
        

