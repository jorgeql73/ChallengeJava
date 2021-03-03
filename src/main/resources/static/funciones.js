/* global Swal, result, swalWithBootstrapButtons, swal */


function eliminar(id){
    const swalWithBootstrapButtons = Swal.mixin({
    customClass: {
      confirmButton: 'btn btn-success',
      cancelButton: 'btn btn-danger'
    },
    buttonsStyling: false
  });

    swalWithBootstrapButtons.fire({
      title: 'Are you sure?',
      text: "You won't be able to revert this!",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel!',
      reverseButtons: true
    }).then((result) => {
      if (result.isConfirmed) {
          $.ajax({
              url:"delete/"+id,
              success: function(res){
                  console.log(res);
              }
            });
        swalWithBootstrapButtons.fire(
          'Deleted!',
          'Your file has been deleted.',
          'success'
        ).then((OK)=>{
                if(OK){
                 location.href="/inscriptions"; 
              }
          });
      } else if (
        /* Read more about handling dismissals below */
        result.dismiss === Swal.DismissReason.cancel
      ) {
        swalWithBootstrapButtons.fire(
          'Cancelled',
          'Your imaginary file is safe :)',
          'error'
        );
      }
    });
}
$(function() {
  
  var menues = $(".nav li"); 

  menues.click(function() {
     
     menues.removeClass("active");    
     $(this).addClass("active");
  });

});


