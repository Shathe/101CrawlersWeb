(function(){
  var app = angular.module('crawlers',['crawlers']);
  /* Controlador general */
  app.controller('ContainerController',['$scope', '$http','$log',function($scope, $http,$log){
	    // Variables
	    this.tab=2;
	    this.loged=false;
	    this.user=getCookie("usuario");
	    this.email="";
	    this.projectSelected={};

	    /*
	     * GENERAL
	     */
	    /** Sets tab value */
	    this.selectedTab = function(tab){
	      this.tab=tab;
	    };
	    /**Returns true if the checktab is the same as tab */
	    this.isSelected = function(checkTab){
	      return this.tab === checkTab;
	    };
	    /** Gives value to the user variable */  
	    this.setUser = function(){
	      this.user=document.getElementById("usernameLogin").value;
	      loged=true;
	    };

	    /** Sets values for the visual status when the users logsouts */
	    this.salir = function(){
	      this.loged=false;
	      this.tab=2;
	      document.getElementById("liLogin").style.display="inline";
	      document.getElementById("misCrawMenu").style.display="none";
	      document.getElementById("liUser").style.display="none";
	      deleteCookie("idUser");

	    };
	    
	    
	    
	    
	    /*
	     * PROJECS
	     */
	    
	    /** Gets the projects of a user  */
	    this.ProjectsOfUser = function (){
	        /* Con AJAX comprobar que se loguea bien */
	        $.get('/projects',{ idUser: getCookie("idUser")})
	        .done(function(data, status) {
	        //Gets both in the variable and the scope the projects
	        this.projects=data;
            $scope.projects=data;
            $scope.$apply();
	        })
	        .fail(function(data, status) {
	          console.log(data);
	        });
	      };
	      /** Creates a project and shows the new projects if it has been really created */
	      $scope.createProject = function() {

	    	  $.post('/createProject',{ idUser: getCookie("idUser"),
	    			  dslPath: document.getElementById("projectDSLPath").value,
	    			  name: document.getElementById("projectName").value,
	    			  pluginsPath: document.getElementById("projectPluginsPath").value})
	    	    .done(function(data, status) {
	    	    	console.log(data);
	    			$scope.projects.push(data);
		  	        $scope.$apply();

	    	    })
	    	    .fail(function(data, status) {
	    	      console.log(data);	  			
	    	      alert('Not possible to connect to the server');
	    	      
	    	    });
	        	$('#edit').modal('toggle');

	      }
	      /* La diferencia entre $scope y this. es que con scope
	         se necesita hacer el scope.apply y es mejor para actaulizar cosas visuales
	         como por ejemplo despues de hacer una peticion http
	      	 en cambio si quieres ligar valores para utilizarlos es mejor el this.
	       	aqui muestro un ejemplo de como se podría hacer con ambas */
	      
	      /** Goes to a project (visually) */

	      $scope.goToProject = function(idProject) {
	    	  alert('project clicked '+idProject);
	      }
	      /** Deletes a project and shows visually if it has been really deleted */

	      this.deleteProject = function(project) {
	        	  $.ajax({
		    		  type: 'DELETE',
		    		  dataType: 'json',
		    		  contentType:'application/json',
		    		  url: "/deleteProject",
		    		  data:JSON.stringify(project),
		    		  success : function(data) {
		  	    	    index= $scope.projects.indexOf(project);
		  	        	$scope.projects.splice(index, 1);
		  	        	$scope.$apply();
			  			console.log('deleted'+project.name);

		  			},
		  			error : function(e) {
		  				alert('Not possible to connect to the server');
		  			  console.log('not deleted'+project.name);

		  			},
		  			
		    		  });
		    	 
		        	$('#delete').modal('toggle');
	      }
	      /** Edits a project and shows the new projects if it has been really edited */

	      this.editProject = function(project) {
	    	  if(jQuery.isEmptyObject(project)){
	    		  //If the edits really means create
	    		  $scope.createProject();	    	  
	    		 }
	    	  else{
	    		  //Gets former values
	    	  var nombreAnterior=project.name;
	    	  var dslAnterior=project.dslPath;
	    	  project.name=document.getElementById("projectName").value;
	    	  project.dslPath=document.getElementById("projectDSLPath").value;
	    	  $.ajax({
	    		  type: 'POST',
	    		  dataType: 'json',
	    		  contentType:'application/json',
	    		  url: "/editProject",
	    		  data:JSON.stringify(project),
	    		  success : function(data) {
	    			  
	  			},
	  			error : function(e) {
		    		  //If there's an error reset former values
	  				project.name=nombreAnterior;
	  				project.dslPath=dslAnterior;
	  				alert('Not possible to connect to the server');
	  			},
	  			
	    		  });
	    	 
	        	$('#edit').modal('toggle');
	    	  }

	      }
		  /** Resets Project Modal values */
	      this.vaciarCamposEditModal = function(){
	    	  document.getElementById("projectName").value="";
	    	  document.getElementById("projectDSLPath").value="";
	    	  document.getElementById("projectPluginsPath").value="";
	      }
		  /** Edits Project Modal values */
	      this.setCamposEditModal= function(project){
	    	  document.getElementById("projectName").value=project.name;
	    	  document.getElementById("projectDSLPath").value=project.dslPath;
	    	  document.getElementById("projectPluginsPath").value=project.pluginsPath;
	      }

	  }]);



  
  /*
   * REGISTER AND LOGIN
   */
  app.controller('RegistroLoginCtrl',['$scope', '$http','$log',function($scope, $http,$log){
    this.user=document.getElementById("usernameLogin").value;
    this.email=document.getElementById("emailRegister").value;
    this.password=document.getElementById("passwordLogin").value;
    this.repeatPswd="";

	  /** Gives red color if the passwords are not the same */
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

    /** Register a user if the fields are ok and if the server dont send an error */
    
    this.comprobarCamposRegistro = function (valido){
      if (valido){
    	  //if fileds are valid
        if (this.repeatPswd==this.password){
          //if password ok
          $.post('/registro',{ user: this.user, pass: this.password, email: this.email})
          .done(function(data, status) {
            //Registered
            document.getElementById("usernameLogin").value=document.getElementById("usernameRegister").value;
            document.getElementById("passwordLogin").value=document.getElementById("passwordRegister").value;
            $('#login-form-link').click();
          })
          .fail(function(data, status) {
            var obj = jQuery.parseJSON(data.responseText );
            var mensajeError=obj.message;
            if(mensajeError.indexOf("email") > -1){
              document.getElementById("emailRegister").value="";
              document.getElementById("emailRegister").placeholder="Email in use";
            }
            if(mensajeError.indexOf("user") > -1){
              document.getElementById("usernameRegister").value="";
              document.getElementById("usernameRegister").placeholder="username already in use, try a new one";

            }
            if(mensajeError.indexOf("Fill") > -1){
              if(mensajeError.indexOf("email") > -1){
                document.getElementById("emailRegister").placeholder="Fill the email out";
              }
              if(mensajeError.indexOf("user") > -1){
                document.getElementById("usernameRegister").placeholder="Fill the user out";
              }
              if(mensajeError.indexOf("password") > -1){
                document.getElementById("passwordRegister").placeholder="Fill the password out";
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
    /** Logs a user if daa is OK */
    this.comprobarCamposLogin = function (){
      /* Con AJAX comprobar que se loguea bien */
      this.user=document.getElementById("usernameLogin").value;
      this.password=document.getElementById("passwordLogin").value;
      loginUser(this.user, this.password);
      $("[data-toggle=tooltipProject]").tooltip();

    };



  }]);

  /** DIRECTIVES */
  app.directive("contacto", function() {
    return {
      restrict: 'E',
      templateUrl: "contacto.html"
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
 * AUXILIAR FUNCTIONS
 */

$(function() {
  // Default values
  document.getElementById("rememberMe").checked=true;
  /* Se intentan recuperar las cookies */
    var user=getCookie("usuario");
    var pass=getCookie("password");
    if(user === "" ){
    	//
    }
    else{
        document.getElementById("usernameLogin").value=user;
        document.getElementById("passwordLogin").value=pass;
        // If there ar cookies try to automatically log the user
        loginUser(user, pass);

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
/** Logs in a user if the data is OK */
function loginUser(username, pass) {
    $.post('/login',{ user: username, pass:pass})
    .done(function(data, status) {
    	// LOG OK, visual changes and value changes
    	$(".modal-backdrop").remove();
    	$("#myModal").hide();
    	$("#myModal").click();
    	document.getElementById("liLogin").style.display="none";
    	document.getElementById("liUser").style.display="inline";
    	document.getElementById("misCrawMenu").style.display="inline";
      if(document.getElementById("rememberMe").checked){
        setCookie("usuario", document.getElementById("usernameLogin").value , 30);
        setCookie("password", document.getElementById("passwordLogin").value , 30);
      }
      else{
        deleteCookie("usuario");
        deleteCookie("password");
      }
      //Sets id user 
      setCookie("idUser", data, 30);


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

}
// Sets a cookie 
function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
    console.log(cname + "=" + cvalue + "; " + expires);
}

// Get a cookie 
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
//Delete cookie
var deleteCookie = function(name) {
    document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
};

