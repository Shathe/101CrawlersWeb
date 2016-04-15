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
	    this.imageSelected={};
	    this.containerSelected={};
	    this.formerName="";
	    this.ListProjects=1;
	    
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
	    this.selectedListProjects = function(tab){
	    	this.ListProjects=tab;
		    };
		    /**Returns true if the checktab is the same as tab */

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
	    this.showProjects = function (){
	    	return this.ListProjects==1  && this.isSelected(1) ;
	    }
	    this.showImages = function (){
	    	return this.ListProjects==2 && this.isSelected(1) ;
	    }
	    this.showContainers = function (){
	    	return this.ListProjects==3 && this.isSelected(1) ;
	    }
	    this.showContainerItem = function (){
	    	console.log(this.ListProjects==4 && this.isSelected(1));

	    	return this.ListProjects==4 && this.isSelected(1) ;
	    }
	    
	    

	    /*
	     * PROJECS
	     */
	    
	    /** Gets the projects of a user  */
	    this.ProjectsOfUser = function (){
	        $.get('/projects',{ idUser: getCookie("idUser")})
	        .done(function(data, status) {
	        //Gets both in the variable and the scope the projects
			    console.log(data);
		        this.projects=data;
	            $scope.projects=data;
	            $scope.$apply();
	        })
	        .fail(function(data, status) {
	          console.log(data);
	        });
	      };
	      /** Creates a project and shows the new projects if it has been really created */
	      this.createProject = function() {

	    	  $.post('/createProject',{ idUser: getCookie("idUser"),
    			  name: document.getElementById("projectName").value})
    	    .done(function(data, status) {
    	    	console.log(data);
    			$scope.projects.push(data);
	  	        $scope.$apply();
    		    this.projectSelected=data;
	        	$('#edit').modal('toggle');
	        	//Creates tit configuration
		    	 $.post('/createConfiguration',{ idProject: this.projectSelected.id,
			    	  dslPath: document.getElementById("projectDSLPath").value,
					  pluginsPath: document.getElementById("projectPluginsPath").value})
	    	    .done(function(data, status) {
	    	    	console.log(data);
	    	    })
	    	    .fail(function(data, status) {
	    	      console.log(data);	  			
	    	      alert('Not possible to connect to the server');
	    	      
	    	    });

    	    })
    	    .fail(function(data, status) {
    	      console.log(data);	  			
    	      alert('Not possible to connect to the server');
    	      
    	    });
	    	  console.log(this.projectSelected);
	    	  console.log(this.projectSelected.id);

	    	  
	    	  
	    	  

	      }
	      /* La diferencia entre $scope y this. es que con scope
	         se necesita hacer el scope.apply y es mejor para actaulizar cosas visuales
	         como por ejemplo despues de hacer una peticion http
	      	 en cambio si quieres ligar valores para utilizarlos es mejor el this.
	       	aqui muestro un ejemplo de como se podría hacer con ambas */
	      
	      /** Goes to a project (visually) */

	      this.goToProject = function(project) {
	    	  angular.copy(project, this.projectSelected);
	    	  this.ImagesOfaProject();
	    	  
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
			        	$('#delete').modal('toggle');

		  			},
		  			error : function(e) {
		  				alert('Not possible to connect to the server');
		  			  console.log('not deleted'+project.name);

		  			},
		  			
		    		  });
		    	 
	      }
	      /** Edits a project and shows the new projects if it has been really edited */

	      this.editProject = function(project) {
	    	  if(jQuery.isEmptyObject(project)){
	    		  //If the edits really means create
	    		  this.createProject();	    	  
	    		 }
	    	  else{
	    		  //Gets former values
 
	    	  project.name=document.getElementById("projectName").value;
	    	  console.log(project);
	    	  $.ajax({
	    		  type: 'POST',
	    		  dataType: 'json',
	    		  contentType:'application/json',
	    		  url: "/editProject",
	    		  data:JSON.stringify(project),
	    		  success : function(data) {
	    			  //now create the configuration 
	    			 $.post('/createConfiguration',{ idProject: project.id,
	    		    	  dslPath: document.getElementById("projectDSLPath").value,
	    				  pluginsPath: document.getElementById("projectPluginsPath").value})
	    	    	    .done(function(data, status) {
	    	    	    	console.log(data);
	    		        	$('#edit').modal('toggle');

	    	    	    })
	    	    	    .fail(function(data, status) {
	    	    	      console.log(data);	  			
	    	    	      alert('Not possible to connect to the server');
	    	    	      
	    	    	    });
	  			},
	  			error : function(e) {
		    		  //If there's an error reset former values
	  				project.name=this.formerName;
	  				alert('Not possible to connect to the server');
	  			},
	  			
	    		  });
	    	 
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
	    	  this.formerName=project.name; 

	    	  document.getElementById("projectName").value=project.name;
	    	  //get configuration
		        $.get('/configuration',{ idProject: project.id})
		        .done(function(data, status) {
		        //Gets both in the variable and the scope the projects
		          console.log(data);
		    	  document.getElementById("projectDSLPath").value=data.dslPath;
		    	  document.getElementById("projectPluginsPath").value=data.pluginsPath;
		        })
		        .fail(function(data, status) {
		        	document.getElementById("projectDSLPath").placeholder="Not possible to connect to the server";
			    	document.getElementById("projectPluginsPath").placeholder="Not possible to connect to the server";

		          console.log(data);
		        });
	    	  /**/
	      }

  
  

  /*
   * IMAGES
   */
  
  /** Gets the images of a project  */
  this.ImagesOfaProject = function (){
      $.get('/images',{ idProject: this.projectSelected.id})
      .done(function(data, status) {
		  console.log(data);
          $scope.images=data;
          $scope.$apply();
      })
      .fail(function(data, status) {
        console.log(data);
      });
    };
    
    /** Creates a image and shows the new image if it has been really created */
    this.createImage = function() {

  	  $.post('/createImage',{ idProject: this.projectSelected.id,
			  name: document.getElementById("imageName").value})
	    .done(function(data, status) {
	    	console.log(data);
			$scope.images.push(data);
	        $scope.$apply();
	      	$('#editImage').modal('toggle');

	    })
	    .fail(function(data, status) {
	      console.log(data);	  			
	      alert('Not possible to connect to the server');
	      
	    }); 	  
  	  
  	  

    }
    /* La diferencia entre $scope y this. es que con scope
       se necesita hacer el scope.apply y es mejor para actaulizar cosas visuales
       como por ejemplo despues de hacer una peticion http
    	 en cambio si quieres ligar valores para utilizarlos es mejor el this.
     	aqui muestro un ejemplo de como se podría hacer con ambas */
    
    /** Goes to a image (visually) */

    this.goToImage = function(image) {
    	 angular.copy(image, this.imageSelected);
    	this.containersOfAImage();
	  }
    /** Deletes a image and shows visually if it has been really deleted */

    this.deleteImage = function(image) {
      	  $.ajax({
	    		  type: 'DELETE',
	    		  dataType: 'json',
	    		  contentType:'application/json',
	    		  url: "/deleteImage",
	    		  data:JSON.stringify(image),
	    		  success : function(data) {
	  	    	    index= $scope.images.indexOf(image);
	  	        	$scope.images.splice(index, 1);
	  	        	$scope.$apply();
		  			console.log('deleted'+image.name);
		        	$('#DeleteImage').modal('toggle');

	  			},
	  			error : function(e) {
	  				alert('Not possible to connect to the server');
	  			  console.log('not deleted'+image.name);

	  			},
	  			
	    		  });
	    	 
    }
    /** Edits a image and shows the new image if it has been really edited */

    this.editImage = function(image) {
  	  if(jQuery.isEmptyObject(image)){
  		  //If the edits really means create
  		  this.createImage();	    	  
  		 }
  	  else{
  		  //Gets former values

  	  image.name=document.getElementById("imageName").value;
  	  console.log(image);
  	  $.ajax({
  		  type: 'POST',
  		  dataType: 'json',
  		  contentType:'application/json',
  		  url: "/editImage",
  		  data:JSON.stringify(image),
  		  success : function(data) {
  	      	$('#editImage').modal('toggle');

  		  },
			error : function(e) {
	    		  //If there's an error reset former values
				image.name=this.formerName;
				alert('Not possible to connect to the server');
			},
			
  		  });
  	 
  	  }

    }
	  /** Resets image Modal values */
    this.vaciarCamposImages = function(){
  	  document.getElementById("imageName").value="";
    }
	  /** Edits image Modal values */
    this.setCamposEditImage= function(image){
  	  this.formerName=image.name; 
  	  document.getElementById("imageName").value=image.name;
    }
    
    
    
    
    


    /*
     * CONTAINERS
     */
    
    /** Gets the containers of a image  */
    this.containersOfAImage = function (){
        $.get('/containers',{ idImage: this.imageSelected.id})
        .done(function(data, status) {
  		  console.log(data);
            $scope.containers=data;
            $scope.$apply();
        })
        .fail(function(data, status) {
          console.log(data);
        });
      };
      
      /** Creates a Container and shows the new container if it has been really created */
      this.createContainer = function() {

    	  $.post('/createContainer',{ idProject: this.projectSelected.id,
    		  idImage: this.imageSelected.id,
  			  name: document.getElementById("containerName").value})
  	    .done(function(data, status) {
  	    	console.log(data);
  			$scope.containers.push(data);
  	        $scope.$apply();
  	      	$('#editContainer').modal('toggle');

  	    })
  	    .fail(function(data, status) {
  	      console.log(data);	  			
  	      alert('Not possible to connect to the server');
  	      
  	    }); 	  
    	  

      }
      
      
      /** Goes to a container (visually) */

      this.goToContainer= function(container) {
      	 angular.copy(container, this.containerSelected);
  	  }
      /** Deletes a Container and shows visually if it has been really deleted */

      this.deleteContainer = function(container) {
        	  $.ajax({
  	    		  type: 'DELETE',
  	    		  dataType: 'json',
  	    		  contentType:'application/json',
  	    		  url: "/deleteContainer",
  	    		  data:JSON.stringify(container),
  	    		  success : function(data) {
  	  	    	    index= $scope.containers.indexOf(container);
  	  	        	$scope.containers.splice(index, 1);
  	  	        	$scope.$apply();
  		  			console.log('deleted'+container.name);
  		        	$('#DeleteContainer').modal('toggle');

  	  			},
  	  			error : function(e) {
  	  				alert('Not possible to connect to the server');
  	  			  console.log('not deleted'+container.name);

  	  			},
  	  			
  	    		  });
  	    	 
      }
      /** Edits a Container and shows the new image if it has been really edited */

      this.editContainer = function(container) {
    	  if(jQuery.isEmptyObject(container)){
    		  //If the edits really means create
    		  this.createContainer();	    	  
    		 }
    	  else{
    		  //Gets former values

    	  container.name=document.getElementById("containerName").value;
    	  console.log(container);
    	  $.ajax({
    		  type: 'POST',
    		  dataType: 'json',
    		  contentType:'application/json',
    		  url: "/editContainer",
    		  data:JSON.stringify(container),
    		  success : function(data) {
    	      	$('#editContainer').modal('toggle');

    		  },
  			error : function(e) {
  	    		  //If there's an error reset former values
  				container.name=this.formerName;
  				alert('Not possible to connect to the server');
  			},
  			
    		  });
    	 
    	  }

      }
  	  /** Resets container Modal values */
      this.vaciarCamposContainer = function(){
    	  document.getElementById("containerName").value="";
      }
  	  /** Edits container Modal values */
      this.setCamposEditContainer= function(container){
    	  this.formerName=container.name; 
    	  document.getElementById("containerName").value=container.name;
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
  app.directive("images", function() {
	    return {
	      restrict: 'E',
	      templateUrl: "imagesPage.html"
	    };
	  });
  app.directive("containers", function() {
	    return {
	      restrict: 'E',
	      templateUrl: "containersPage.html"
	    };
	  });
  
  app.directive("containeritem", function() {
	    return {
	      restrict: 'E',
	      templateUrl: "containerItem.html"
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

