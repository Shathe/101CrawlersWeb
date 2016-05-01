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
	    $scope.pluginEdited=false;
	    $scope.dslEdited=false;
	    $scope.containerStatus={};
	    /*
		 * GENERAL
		 */
	    /** Sets tab value */
	    this.selectedTab = function(tab){
		      this.tab=tab;
		    };
		    /** Returns true if the checktab is the same as tab */
		    this.isSelected = function(checkTab){
		      return this.tab === checkTab;
		    };
	    this.selectedListProjects = function(tab){
	    	this.ListProjects=tab;
		    };
		    /** Returns true if the checktab is the same as tab */

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
	    /**
	     * Deletes the  configurations files from the server
	     */
	    this.resetUpload= function (){
	    	 $.post('/resetUploadConfig',{idUser: getCookie("idUser")})
	    	    .done(function(data, status) {
		    	      uploadMessage("Reseted",0);
		    		    $scope.pluginEdited=false;
		    		    $scope.dslEdited=false;

	    	    })
	    	    .fail(function(data, status) {
	    	      console.log(data);	    	        	
  	        	uploadMessage("Could not reset",1);

	  				    	      
	    	    });
	    }

	    /**
	     * uploads the files of a plugin 
	     */
	    this.uploadPlugin= function (){
	    	// The plugin must have a name
	    	if( document.getElementById("pluginName").value==""){
	    		 uploadMessage ("Please, set the plugin name",1);
	    	}else{
	    	 var formData = new FormData($("#pluginForm")[0]);
	    	    $.ajax({
	    	        url: '/pluginForm',
	    	        type: 'POST',
	    	        data: formData,
	    	        async: true,
	    	        success: function (data) {
	    	        	uploadMessage(document.getElementById("pluginName").value+" uploaded, you can add more",0);
	    	        	$scope.pluginEdited=true;
	    	        	document.getElementById("pluginName").value="";
	    	        	document.getElementById("plugin").value="";
	    	        },
	    	        error: function (data) {
	    	        	uploadMessage("Plugin not uploaded, try again. (Max lentgh 10 Mb ",1);
	    	        },
	    	        cache: false,
	    	        contentType: false,
	    	        processData: false
	    	    });

	    	    return false;
	    	}
			 
	    }
	    
	    /**
	     * Uploads the DSL file to the server
	     */
	    this.uploadDSL= function (){

	    	 var formData = new FormData($("#dslForm")[0]);

	    	    $.ajax({
	    	        url: '/uploadDSL',
	    	        type: 'POST',
	    	        data: formData,
	    	        async: true,
	    	        success: function (data) {
	    	        	uploadMessage("DSL uploaded",0);
	    	        	document.getElementById("plugin").value="";
	    	        	$scope.dslEdited=true;
	    	        }, 
	    	        error: function (data) {
	    	        	uploadMessage("DSL not uploaded, try again",1);
	    	        },
	    	        cache: false,
	    	        contentType: false,
	    	        processData: false
	    	    });

	    	    return false;
			 
	    }

	    /*
		 * PROJECS
		 */
	    
	    /** Gets the projects of a user */
	    this.ProjectsOfUser = function (){
	        $.get('/projects',{ idUser: getCookie("idUser")})
	        .done(function(data, status) {
	        // Gets both in the variable and the scope the projects
			    console.log(data);
		        this.projects=data;
	            $scope.projects=data;
	            $scope.$apply();
	        })
	        .fail(function(data, status) {
	          console.log(data);
	        });
	      };
	      /**
			 * Creates a project and shows the new projects if it has been
			 * really created
			 */
	      this.createProject = function() {
	    	  $scope.projectnotcreated=false;
	    	  project=this.projectSelected;
	    	  // The project must have a dsl file
	    	  if($scope.dslEdited==true){
			    	  $.post('/createProject',{ idUser: getCookie("idUser"),
		    			  name: document.getElementById("projectName").value})
		    	    .done(function(data, status) {
		    	    	console.log(data);
		    		    projectSelected=data;

			        	// Creates it configuration
					    	 $.post('/createConfiguration',{ idProject: projectSelected.id})
				    	    .done(function(data, status) {
				    	    	console.log(data);
				    	    	// Once you upload the files, you must save them
		    		    	      uploadMessage("Validating configuration...",0);

				    	    	$.post('/saveConfigurationFiles',{ idUser: getCookie("idUser"),
				    	    		idProject:data.idProject,idConfig:data.id })
					    	    .done(function(data, status) {
					    	    	console.log(data);
					    	    	$scope.pluginEdited=false;
		  	    		    	    $scope.dslEdited=false;
						        	$('#edit').modal('toggle');
						        	$scope.projects.push(projectSelected);
						  	        $scope.$apply();
						        	})
						        	.fail(function(data, status) {
						        		//failing loading files, delete the unvalid project
							    	      console.log(data);	  			
				    		    	      uploadMessage(data.responseJSON.message,1);
				    		    	      $.ajax({
								    		  type: 'DELETE',
								    		  dataType: 'json',
								    		  contentType:'application/json',
								    		  url: "/deleteProject",
								    		  data:JSON.stringify(projectSelected),
								    		  success : function(data) {
								  				}  			
								    		  });
							        	  	
						        	  });
		
				    	    })
				    	    .fail(function(data, status) {
				        		//failing loading files, delete the unvalid project
				    	      console.log(data);
	    		    	      uploadMessage(data.responseJSON.message,1);
	    		    	      $.ajax({
					    		  type: 'DELETE',
					    		  dataType: 'json',
					    		  contentType:'application/json',
					    		  url: "/deleteProject",
					    		  data:JSON.stringify(projectSelected),
					    		  success : function(data) {
					  				}  			
					    		  });
				        	  
				    	    });

		    	    })
		    	    .fail(function(data, status) {
		    	      console.log(data);	  			
		    	      alert('Not possible to connect to the server');
		    	      
		    	    });
    	    }else{
  			  uploadMessage ("upload the dsl, please",1);
    	    }

	      }
	      
	      /** Goes to a project (visually) */

	      this.goToProject = function(project) {
	    	  this.projectSelected=project;
	    	  console.log(this.projectSelected);
	    	  this.ImagesOfaProject();
	      }
	      /**
			 * Deletes a project and shows visually if it has been really
			 * deleted
			 */

	      this.deleteProject = function() {
	    	  var project=this.projectSelected;
	        	  $.ajax({
		    		  type: 'DELETE',
		    		  dataType: 'json',
		    		  contentType:'application/json',
		    		  url: "/deleteProject",
		    		  data:JSON.stringify(project),
		    		  success : function(data) {
		    			console.log(this.projectSelected);
		  	    	    index= $scope.projects.indexOf(project);
		  	        	$scope.projects.splice(index, 1);
		  	        	$scope.$apply();
			        	$('#delete').modal('toggle');

		  			},
		  			error : function(e) {
		  				alert('Not possible to connect to the server');
		  				console.log('not deleted'+project.name);
		  			}
		  			
		    		  });
		    	 
	      }
	      /**
			 * Edits a project and shows the new projects if it has been really
			 * edited
			 */

	      this.editProject = function() {
	    	 var project=this.projectSelected;
	    	if(document.getElementById("projectName").value!=""){
		    	  if(jQuery.isEmptyObject(project)){
		    		  // If the edits really means create
		    		  this.createProject();	    	  
		    		 }
		    	  else{
		    		  // Gets former values
	 
		    	  this.projectSelected.name=document.getElementById("projectName").value;
		    	  $scope.projectSelected=project.name;
		    	  console.log(project);
		    	  $.ajax({
		    		  type: 'POST',
		    		  dataType: 'json',
		    		  contentType:'application/json',
		    		  url: "/editProject",
		    		  data:JSON.stringify(project),
		    		  success : function(data) {
		    			  console.log($scope.dslEdited);
		    			  if( $scope.dslEdited==true){
		    				  
		    			// If there's something to edit in the configuration, you create a new one
		    			 $.post('/createConfiguration',{ idProject: project.id})
		    	    	    .done(function(data, status) {
		    	    	    	console.log(data);
		    		    	      uploadMessage("Validating configuration...",0);

				    	    	// Once you upload the files, you must save them
		    	    	    	$.post('/saveConfigurationFiles',{ idUser: getCookie("idUser"),
		    	    	    		idProject:data.idProject,idConfig:data.id })
		    		    	    .done(function(data, status) {
		    		    	    	console.log(data);
		    		    	    	$scope.pluginEdited=false;
		  	    		    	    $scope.dslEdited=false;
		    			        	$('#edit').modal('toggle');
	
		    		    	    })
		    		    	    .fail(function(data, status) {
		    		    	      console.log(data);	  			
		    		    	      uploadMessage(data.responseJSON.message,1);
		    		    	      
		    		    	    });
		    	    	    })
		    	    	    .fail(function(data, status) {
		    	    	      console.log(data);	  			
		    	    	      alert('Not possible to connect to the server');
		    	    	      
		    	    	    });
		    			 // If there are plugins uploaded but there is not dsl upload
		    		  }else if($scope.pluginEdited==true  && $scope.dslEdited==false){
		    			  uploadMessage ("upload the dsl, please or reset it",1);
		    		  }
		    		else{
	  			        	$('#edit').modal('toggle');
		    			  }
		  			},
		  			error : function(e) {
			    		  // If there's an error reset former values
		  				this.projectSelected.name=this.formerName;
		  				alert('Not possible to connect to the server');
		  			},
		  			
		    		  });
		    	 
		    	  }
		    	  console.log($scope.dslEdited);
		      }else{
		    	  document.getElementById("projectName").placeholder="Plase, give me a name";
		      }
	      }
		  /** Resets Project Modal values */
	      this.vaciarCamposEditModal = function(){
	    	  document.getElementById("projectName").value="";
	    	  document.getElementById("idUserDSL").value=getCookie("idUser");
	    	  document.getElementById("idUserPlugin").value=getCookie("idUser");
	    	  document.getElementById("pluginName").value="";
	    	  emptyUploadMessage();
	    	}
		  /** Edits Project Modal values */
	      this.setCamposEditModal= function(project){
	    	  formerName=project.name; 
	    	  document.getElementById("idUserDSL").value=getCookie("idUser");
	    	  document.getElementById("idUserPlugin").value=getCookie("idUser");
	    	  document.getElementById("projectName").value=project.name;
	    	  document.getElementById("pluginName").value="";
	    	  emptyUploadMessage();
	    	  
	      }

  
  

	 /*
	 * IMAGES
	 */
  
  /** Gets the images of a project */
  this.ImagesOfaProject = function (){
	  console.log(this.projectSelected);
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
	  	    emptyimageMessage();


	    })
	    .fail(function(data, status) {
	      console.log(data);	  
	      imageMessage(data.responseJSON.message,1);	      
	    }); 	  
  	  
  	  

    }
    /*
	 * La diferencia entre $scope y this. es que con scope se necesita hacer el
	 * scope.apply y es mejor para actaulizar cosas visuales como por ejemplo
	 * despues de hacer una peticion http en cambio si quieres ligar valores
	 * para utilizarlos es mejor el this. aqui muestro un ejemplo de como se
	 * podría hacer con ambas
	 */
    
    /** Goes to a image (visually) */

    this.goToImage = function(image) {
    	this.imageSelected=image;
    	this.containersOfAImage();
	  }
    /** Deletes a image and shows visually if it has been really deleted */

    this.deleteImage = function() {
    	var image=this.imageSelected;
    	var project=this.projectSelected;

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

    this.editImage = function() {
    	var image=this.imageSelected;
    	if(document.getElementById("imageName").value!=""){	     
	  	  if(jQuery.isEmptyObject(image)){
	  		  // If the edits really means create
	  		  imageMessage("The image is been building, it can take a while...",0);
	  		  this.createImage();	    	  
	  		 }
	  	  else{
	  		  // Gets former values
	
	  		  this.imageSelected.name=document.getElementById("imageName").value;
	  	  $.ajax({
	  		  type: 'POST',
	  		  dataType: 'json',
	  		  contentType:'application/json',
	  		  url: "/editImage",
	  		  data:JSON.stringify(image),
	  		  success : function(data) {
	  	      	$('#editImage').modal('toggle');
	    	      emptyimageMessage();
	  		  },
				error : function(e) {
		    		  // If there's an error reset former values
					this.imageSelected.name=this.formerName;
					imageMessage(data.responseJSON.message,1);				},
				
	  		  });
	  	 
	  	  }
    	}else{
	    	  document.getElementById("imageName").placeholder="Plase, give me a name";
    	}
    }
	  /** Resets image Modal values */
    this.vaciarCamposImages = function(){
  	  document.getElementById("imageName").value="";
    }
	  /** Edits image Modal values */
    this.setCamposEditImage= function(image){
  	  formerName=image.name; 
  	  document.getElementById("imageName").value=image.name;
    }
    
    
    
    
    


    /*
	 * CONTAINERS
	 */
    
    /** Gets the containers of a image */
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
      
      /**
		 * Creates a Container and shows the new container if it has been really
		 * created
		 */
      this.createContainer = function() {
    	  containerMessage("Creating container...",0);
    	  $.post('/createContainer',{ idProject: this.projectSelected.id,
    		  idImage: this.imageSelected.id,
  			  name: document.getElementById("containerName").value})
  	    .done(function(data, status) {
  	    	console.log(data);
  			$scope.containers.push(data);
  	        $scope.$apply();
  	      	$('#editContainer').modal('toggle');
  	      emptyContainerMessage();

  	    })
  	    .fail(function(data, status) {
  	      console.log(data);	  			
  	      containerMessage (data.responseJSON.message,1) 	;  
  	      
  	    }); 	  
    	  

      }
      
      
      /** Goes to a container (visually) */

      this.goToContainer= function(container) {
	      emptyContainerItemMessage();
	  	  this.containerSelected=container;
	  	 $scope.containerStatus={containerStatus:"Loading..",
	  			 crawlerStatus:"Loading..",rounds:"Loading..",
	  	    	 fetchedLinks:"Loading..", unfetchedLinks:"Loading.."};
      }
      /** Deletes a Container and shows visually if it has been really deleted */

      this.deleteContainer = function() {
    	  var container=this.containerSelected;
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
      /**
		 * Edits a Container and shows the new image if it has been really
		 * edited
		 */

	      this.editContainer = function() {
	    	var container=this.containerSelected;
	      	if(document.getElementById("containerName").value!=""){	     

	    	  if(jQuery.isEmptyObject(container)){
	    		  // If the edits really means create
	    		  this.createContainer();	    	  
	    		 }
	    	  else{
	    		  // Gets former values
	
	    		  this.containerSelected.name=document.getElementById("containerName").value;
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
		  	    		  // If there's an error reset former values
		  				this.containerSelected.name=this.formerName;
		  				alert('Not possible to connect to the server');
		  			},
		  			
	    		  });
	    	 
	    	  }
	      }else{
	      	document.getElementById("containerName").value!="Please, give me a name";
	      }
      }
  	  /** Resets container Modal values */
      this.vaciarCamposContainer = function(){
    	  document.getElementById("containerName").value="";
      }
  	  /** Edits container Modal values */
      this.setCamposEditContainer= function(container){
    	  formerName=container.name; 
    	  document.getElementById("containerName").value=container.name;
      }

      
      
        
        /** stops the container */
        this.stopContainer = function (){
      	  var container=this.containerSelected;
      	 containerItemMessage("Stopping the container",0)

            $.post('/stopContainer',{ idContainer: container.id})
            .done(function(data, status) {
            emptyContainerItemMessage();
      		  console.log(data);
              containerItemMessage("Loading status..",0);
              $http({
  		        method : "GET",
  		        url : "/statusContainer",
        	    params:  { idContainer: container.id }
  		    }).then(function mySucces(response) {
                emptyContainerItemMessage();
  		    	console.log(response);
        		$scope.containerStatus=response.data;
        		


  		    }, function myError(response) {
  		    	console.log(response);
          		

  		    });		
            })
            .fail(function(data, status) {
              console.log(data);
              containerItemMessage(data.responseJSON.message,1)
            });              

          };

          /** Pauses the container */
          this.pauseContainer = function (){
        	  var container=this.containerSelected;
           	 containerItemMessage("Paussing the container",0)

              $.post('/pauseContainer',{ idContainer: container.id})
              .done(function(data, status) {
        		  console.log(data);
                  containerItemMessage("Loading status...",0);

                  $http({
      		        method : "GET",
      		        url : "/statusContainer",
            	    params:  { idContainer: container.id }
      		    }).then(function mySucces(response) {
      		    	
      		    	console.log(response);
            		  $scope.containerStatus=response.data;
                      emptyContainerItemMessage();

      		    }, function myError(response) {
      		    	console.log(response);
              		$scope.containerStatus=response.data;
                    emptyContainerItemMessage();

      		    });		
              })
              .fail(function(data, status) {
                console.log(data);
                alert(data.responseJSON.message);
                containerItemMessage(data.responseJSON.message,1)

              });
            };
          
          
          /** Starts the container */
          this.startsContainer = function (){
           	 containerItemMessage("Restarting the container",0)

        	  var container=this.containerSelected;

              $.post('/startsContainer',{ idContainer: container.id})
              .done(function(data, status) {
        		  console.log(data);
                  containerItemMessage("Loading status...",0);
        		    $http({
        		        method : "GET",
        		        url : "/statusContainer",
                	    params:  { idContainer: container.id }
        		    }).then(function mySucces(response) {
        		    	console.log(response);
              		  $scope.containerStatus=response.data;
                      emptyContainerItemMessage();

        		    }, function myError(response) {
        		    	console.log(response);
                		$scope.containerStatus=response.data;
                        emptyContainerItemMessage();

        		    });		  
        		  
              })
              .fail(function(data, status) {
                console.log(data);
                alert(data.responseJSON.message);
                containerItemMessage(data.responseJSON.message,1)

              });
            };
            
          
          /** Gets the status of the container */
          this.containerStatus = function (){
        	  var container=this.containerSelected;
		    	console.log(container);
		    	 $scope.containerStatus={containerStatus:"Loading..",
			  			 crawlerStatus:"Loading..",rounds:"Loading..",
			  	    	 fetchedLinks:"Loading..", unfetchedLinks:"Loading.."};
    	
        	  $http({
  		        method : "GET",
  		        url : "/statusContainer",
        	    params:  { idContainer: container.id }

  		    }).then(function mySucces(response) {
  		    	console.log(response);
        		  $scope.containerStatus=response.data;


  		    }, function myError(response) {
  		    	console.log(response);
          		$scope.containerStatus=response.data;

  		    });	
              
            };
            
            /** Indexes the crawler */
            this.indexCrawl = function (){
                $.post('/indexContainer',{ idContainer: this.containerSelected.id})
                .done(function(data, status) {
          		  console.log(data);
                })
                .fail(function(data, status) {
                  console.log(data);
                });
              };
              
              /** Searches the crawler */
              this.searchQuery = function (){
            	    emptyContainerItemMessage();

                  $http({
        		        method : "GET",
        		        url : "/searchContainer",
              	    params:  { idContainer: this.containerSelected.id ,query : document.getElementById("search").value}

        		    }).then(function mySucces(response) {
        		    	console.log(response);
              		  $scope.results=response.data;
        		    }, function myError(response) {
        		    	console.log(response);
                        containerItemMessage(response.data.responseText,1);

        		    });	
                  
                };
                /** Downloads all the data from the crawler */
                this.downloadAll = function (){
                   
                    $http({
          		        method : "GET",
          		        url : "/downloadAll",
                	    params:  { idContainer: this.containerSelected.id }

          		    }).then(function mySucces(response) {
         		    	  document.location = 'data:Application/octet-stream,' +
                          encodeURIComponent(response.data);
          		    }, function myError(response) {
          		    	console.log(response);

          		    });	
                    
                  };
                  /** download all the results from the query */
                  this.downloadResults = function (){
                      $http({
            		        method : "GET",
            		        url : "/downloadResults",
                  	    params:  { idContainer: this.containerSelected.id ,query : document.getElementById("search").value}

            		    }).then(function mySucces(response) {
           		    	  document.location = 'data:Application/octet-stream,' +
                              encodeURIComponent(response.data);
            		    	
            		    }, function myError(response) {
            		    	console.log(response);

            		    });	
                      
                    };
                    
                

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
      /* Se evalua que las contrasenas coincidas */
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
    	  // if fileds are valid
        if (this.repeatPswd==this.password){
          // if password ok
          $.post('/registro',{ user: this.user, pass: this.password, email: this.email})
          .done(function(data, status) {
            // Registered
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
      // Sets id user
      setCookie("idUser", data, 30);


    })
    .fail(function(data, status) {
      console.log(data);
      var obj = jQuery.parseJSON(data.responseText );
      var mensajeError=obj.message;
      if(mensajeError.indexOf("password") > -1){
        document.getElementById("passwordLogin").value="";
        document.getElementById("passwordLogin").placeholder=obj.message;

      }
      if(mensajeError.indexOf("user") > -1){
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
function uploadMessage (message,error){
	if(error){
		   document.getElementById('messageUpload').innerHTML="<h4   style=\"text-align:center;\" class=\"bg-warning\"><br/><p>"+message+"<p/><br/></h4>";
	}else{
		   document.getElementById('messageUpload').innerHTML="<h4 style=\"text-align:center;\" class=\"bg-success\"> <br/><p>"+message+"<p/><br/></h4>";
	}
	   
}
function imageMessage (message,error){
	if(error){
		   document.getElementById('messageImage').innerHTML="<h4   style=\"text-align:center;\" class=\"bg-warning\"><br/><p>"+message+"<p/><br/></h4>";
	}else{
		   document.getElementById('messageImage').innerHTML="<h4 style=\"text-align:center;\" class=\"bg-success\"> <br/><p>"+message+"<p/><br/></h4>";
	}
	   
}

function containerMessage (message,error){
	if(error){
		   document.getElementById('messageContainer').innerHTML="<h4   style=\"text-align:center;\" class=\"bg-warning\"><br/><p>"+message+"<p/><br/></h4>";
	}else{
		   document.getElementById('messageContainer').innerHTML="<h4 style=\"text-align:center;\" class=\"bg-success\"> <br/><p>"+message+"<p/><br/></h4>";
	}	   
}

function containerItemMessage (message,error){
	if(error){
		   document.getElementById('messageItemContainer').innerHTML="<h4   style=\"text-align:center;\" class=\"bg-warning\"><br/><p>"+message+"<p/><br/></h4>";
	}else{
		   document.getElementById('messageItemContainer').innerHTML="<h4 style=\"text-align:center;\" class=\"bg-success\"> <br/><p>"+message+"<p/><br/></h4>";
	}	   
}

function emptyimageMessage (){
	   document.getElementById('messageImage').innerHTML="";   
}
function emptyUploadMessage (){
	   document.getElementById('messageUpload').innerHTML="";   
}

function emptyContainerMessage (){
	   document.getElementById('messageContainer').innerHTML="";   
}
function emptyContainerItemMessage (){
	   document.getElementById('messageItemContainer').innerHTML="";   
}

// Delete cookie
var deleteCookie = function(name) {
    document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
};


