(function(){
  var app = angular.module('crawlers',['crawlers']);

  /* Controlador general */
  app.controller('ContainerController',function(){
    /* Valores por defecto */
    this.tab=2;
    this.loged=false;
    this.user="";

    /* Edita el valor de la pestaña actual */
    this.selectedTab = function(tab){
      this.tab=tab;
    };
    /* Devuelve si y solo si la pestaña coincide */
    this.isSelected = function(checkTab){
      return this.tab === checkTab;
    };
    /* El usuario se loguea, se cambian valores por defecto */
    this.setUser = function(user){
      this.user=document.getElementById("usernameLogin").value;
      this.loged=true;
      this.tab=1;
    };
    /* El usuario sale de la sesión y se cambian valores y vista visual */
    this.salir = function(){
      this.loged=false;
      this.tab=2;
      document.getElementById("liLogin").style.display="inline";
      document.getElementById("misCrawMenu").style.display="none";
      document.getElementById("liUser").style.display="none";


    };

  });

  /* Controlador del registro y login */
  app.controller('RegistroLoginCtrl',function(){
    this.user="";
    this.email="";
    this.password="";
    this.repeatPswd="";

    /* Actualiza el color del input de la repetición de contraseña cada vez que se edita */
    this.colorPswdRepeat = function (){
      /* Se evalua que las contraseñas coincidas */
      var inputRepeat = document.getElementById("confirmPassword");
      if (this.repeatPswd==this.password){
        inputRepeat.style.borderColor= "#78FA89";
      }
      else{
        inputRepeat.style.borderColor= "#FA787E";
      }
    };

    /* Se comprueba si el usuario se puede registrar correctamete */
    this.comprobarCamposRegistro = function (valido){
      if (valido){
        /* El formulario es valido, hay que comprobar que las contraseñas coincidan
         Y que el usuario no exista ni el email
         */
        if (this.repeatPswd==this.password){
          /* Ccontraseña bien
           Intentar registrar al usuario
           Si hay algun error ponerlo de placeholder en el input correspondiente
           Además cambiar el color del input a rojo
          */



          document.getElementById("usernameLogin").value=document.getElementById("usernameRegister").value;
          document.getElementById("passwordLogin").value=document.getElementById("passwordRegister").value;
          /* Registrarlo en la base de datos */
          $('#login-form-link').click();

        }
        else{
            document.getElementById("confirmPassword").value="";
            document.getElementById("confirmPassword").style.borderColor= "#FA787E";
        }
      }

    };

    this.comprobarCamposLogin = function (){
      /* Con AJAX comprobar que se loguea bien */

      var boolLog=true;
      /* a true será cuando se loguee bien y se hará lo que pone en el fichero Readme.txt
      Si es false, activar mensajes de error */
      if(boolLog){
        /* Debido a bootstrap, hay que hacer esto */
        $(".modal-backdrop").remove();
        $("#myModal").hide();
        $("#myModal").click();
        document.getElementById("liLogin").style.display="none";
        document.getElementById("liUser").style.display="inline";
        document.getElementById("misCrawMenu").style.display="inline";


      }else{
        /* Si hay algun error ponerlo de placeholder en el input correspondiente
        Además cambiar el color del input a rojo */
      }
    };

  });

  app.directive("contacto", function() {
    return {
      restrict: 'E',
      templateUrl: "contacto.html"
      /*
      Si en la directiva quieres poner un controller, luego se le pasa aqui con:
      (4.2 directivas http://campus.codeschool.com/courses/shaping-up-with-angular-js/contents)
      controller:function(){
      definir funcion
      }
      controllerAs:'nombre'
      */
    };
  });

  app.directive("crawlers", function() {
    return {
      restrict: 'E',
      templateUrl: "crawlers.html"
    };
  });
  app.directive("home", function() {
    return {
      restrict: 'E',
      templateUrl: "home.html"
    };
  });
  app.directive("perfil", function() {
    return {
      restrict: 'E',
      templateUrl: "perfil.html"
    };
  });
  app.directive("nosotros", function() {
    return {
      restrict: 'E',
      templateUrl: "nosotros.html"
    };
  });


})();



/*
Peticioes angular

var controller=this;
controller.lista=[];

$http.get('urlpeticion').success(function(data){
controller.lista=Data;
});

$http.get('urlpeticion',{parametro: 'valor'})
tambien hay post, delete...
*/



/* javascript del modal de Regitro/Loguin */
$(function() {

    $('#login-form-link').click(function(e) {
    $("#register-form").fadeOut(100);
		$("#login-form").delay(100).fadeIn(100);
		$('#register-form-link').removeClass('active');
		$(this).addClass('active');
		e.preventDefault();
	});
	$('#register-form-link').click(function(e) {
    $("#login-form").fadeOut(100);
		$("#register-form").delay(100).fadeIn(100);
		$('#login-form-link').removeClass('active');
		$(this).addClass('active');
		e.preventDefault();

	});

});
