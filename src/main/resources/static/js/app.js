(function(){
  var app = angular.module('crawlers',['crawlers']);

  /* Controlador general */
  app.controller('ContainerController',['$scope', '$http','$log',function($scope, $http,$log){
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
    $scope.$on('loged', function(event, args) {
      this.user=document.getElementById("usernameLogin").value;
      this.loged=true;
      this.tab=1;
    });

    /* El usuario sale de la sesión y se cambian valores y vista visual */
    this.salir = function(){
      this.loged=false;
      this.tab=2;
      document.getElementById("liLogin").style.display="inline";
      document.getElementById("misCrawMenu").style.display="none";
      document.getElementById("liUser").style.display="none";


    };

  }]);

  /* Controlador del registro y login */
  app.controller('RegistroLoginCtrl',['$scope', '$http','$log',function($scope, $http,$log){
    this.user=document.getElementById("usernameLogin").value;
    this.email=document.getElementById("emailRegister").value;
    this.password=document.getElementById("passwordLogin").value;
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
          /* Contraseña bien
           Intentar registrar al usuario
           Si hay algun error ponerlo de placeholder en el input correspondiente
           Además cambiar el color del input a rojo
          */
          $.post('/registro',{ user: this.user, pass: this.password, email: this.email})
          .done(function(data, status) {
            /* Usuario registrado con éxito*/
            console.log(data);
            document.getElementById("usernameLogin").value=document.getElementById("usernameRegister").value;
            document.getElementById("passwordLogin").value=document.getElementById("passwordRegister").value;
            $('#login-form-link').click();
          })
          .fail(function(data, status) {
            console.log(data);
            var obj = jQuery.parseJSON(data.responseText );
            var mensajeError=obj.message;
            if(mensajeError.indexOf("email") > -1){
              document.getElementById("emailRegister").value="";
              document.getElementById("emailRegister").placeholder="Email en uso";
            }
            if(mensajeError.indexOf("usuario") > -1){
              document.getElementById("usernameRegister").value="";
              document.getElementById("usernameRegister").placeholder="Nombre de usuario en uso, pruebe con otro";

            }
            if(mensajeError.indexOf("Rellene") > -1){
              if(mensajeError.indexOf("email") > -1){
                document.getElementById("emailRegister").placeholder="Rellene este campo";
              }
              if(mensajeError.indexOf("usuario") > -1){
                document.getElementById("usernameRegister").placeholder="Rellene este campo";
              }
              if(mensajeError.indexOf("contraseña") > -1){
                document.getElementById("passwordRegister").placeholder="Rellene este campo";
              }
            }
          });


        }
        else{
            document.getElementById("confirmPassword").value="";
            document.getElementById("confirmPassword").style.borderColor= "#FA787E";
        }
      }

    };

    this.comprobarCamposLogin = function (){
      /* Con AJAX comprobar que se loguea bien */
      this.user=document.getElementById("usernameLogin").value;
      this.password=document.getElementById("passwordLogin").value;

      $.post('/login',{ user: this.user, pass:this.password})
      .done(function(data, status) {
      	/* Usuario registrado con éxito*/
      	console.log(data);
      	$(".modal-backdrop").remove();
      	$("#myModal").hide();
      	$("#myModal").click();
      	document.getElementById("liLogin").style.display="none";
      	document.getElementById("liUser").style.display="inline";
      	document.getElementById("misCrawMenu").style.display="inline";
      	$scope.$emit('loged', {});
        document.cookie="usuario="+this.user;
        document.cookie="password="+this.password;
        if(document.getElementById("rememberMe").checked){
          setCookie("usuario", document.getElementById("usernameLogin").value , 30);
          setCookie("password", document.getElementById("passwordLogin").value , 30);
        }
        else{
          deleteCookie("usuario");
          deleteCookie("password");
        }
      })
      .fail(function(data, status) {
        console.log(data);
        var obj = jQuery.parseJSON(data.responseText );
        var mensajeError=obj.message;
        if(mensajeError.indexOf("contraseña") > -1){
          document.getElementById("passwordLogin").value="";
          document.getElementById("passwordLogin").placeholder=obj.message;

        }
        if(mensajeError.indexOf("usuario") > -1){
          document.getElementById("usernameLogin").value="";
          document.getElementById("usernameLogin").placeholder=obj.message;

        }
      });
    };

  }]);

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
  //Por defecto se recordaran las cookies
  document.getElementById("rememberMe").checked=true;
  /* Se intentan recuperar las cookies */
    var user=getCookie("usuario");
    var pass=getCookie("password");
    console.log(user);
    console.log(pass);
    if(user === "" ){

    }
    else{
        document.getElementById("usernameLogin").value=user;
        document.getElementById("passwordLogin").value=pass;
    }
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
/* Sets a cookie*/
function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
    console.log(cname + "=" + cvalue + "; " + expires);
}

/* Get a cookie */
function getCookie(cname) {
  console.log( document.cookie);
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
    }
    console.log("Cookie not found");
    return "";
}
var deleteCookie = function(name) {
    document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
};
