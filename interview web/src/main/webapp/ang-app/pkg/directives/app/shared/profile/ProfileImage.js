

Package("directives.app.shared.profile")
.ProfileImage = {
		key: 'profileImage',

		func: function ($http, $q, rootPath) {
			return {
				scope:{
					mode: '=mode',
					msgTime: '=msgtime',
					user: '=user'
					},
				templateUrl: rootPath + 'ang-app/pkg/directives/app/shared/profile/profileImage.html',
				link: function(scope, element, attr){
						console.log("=====Initialize==Link====Directive====Profile image");
						
						scope.addImageError = function(message, level){
							$('#imageMsgList').prepend("<div class='item '> <a class='ui "+((level == 0) ? 'green' : ((level == 1) ? 'orange' : 'red'))+" label'>"+message+"</a></div>");
							setTimeout(function(){
								$('#imageMsgList div:last-child').transition({animation:'fade', onComplete:function(){$(this).remove();}});
							}, scope.msgTime);
						}
						
						
						var dataURLtoBlob = function(dataURL) {
						    var BASE64_MARKER = ';base64,';
						    if (dataURL.indexOf(BASE64_MARKER) == -1) {
						      var parts = dataURL.split(',');
						      var contentType = parts[0].split(':')[1];
						      var raw = decodeURIComponent(parts[1]);

						      return new Blob([raw], {type: contentType});
						    }

						    var parts = dataURL.split(BASE64_MARKER);
						    var contentType = parts[0].split(':')[1];
						    var raw = window.atob(parts[1]);
						    var rawLength = raw.length;

						    var uInt8Array = new Uint8Array(rawLength);

						    for (var i = 0; i < rawLength; ++i) {
						      uInt8Array[i] = raw.charCodeAt(i);
						    }

						    return new Blob([uInt8Array], {type: contentType});
						  }
						
						scope.resize = function(data, max_width, max_height, compression_ratio, imageEncoding, orientation) {
							var 
						    canvas = document.createElement('canvas'),
						    context = null,
						    imageObj = new Image(),
						    blob = null;            

						    //create a hidden canvas object we can use to create the new resized image data
						    canvas.id     = "hiddenCanvas";
						    canvas.width  = max_width;
						    canvas.height = max_height;
						    canvas.style.visibility   = "hidden";   
						    document.body.appendChild(canvas);  

						    //get the context to use 
						    context = canvas.getContext('2d');  

					        imageObj.src = data;


						    // set up the images onload function which clears the hidden canvas context, 
						    // draws the new image then gets the blob data from it
						    imageObj.onload = function() {  

						        // Check for empty images
						        if(this.width == 0 || this.height == 0){
						            alert('Image is empty');
						        } else {                

						            context.clearRect(0,0,max_width,max_height);
						            context.drawImage(imageObj, 0, 0, this.width, this.height, 0, 0, max_width, max_height);

						            blob = dataURLtoBlob(canvas.toDataURL());
//						            console.log(canvas.toDataURL())
						            //pass this blob to your upload function
						            scope.fileToUpload = blob;
						            document.removeChildren(canvas);
//						            upload(blob);
						        }       
						    };

						    imageObj.onabort = function() {
						        alert("Image load was aborted.");
						    };

						    imageObj.onerror = function() {
						        alert("An error occured while loading image.");
						    };

						}
						
						
						scope.getOrientation = function(file, callback) {
						    var reader = new FileReader();
						    reader.onload = function(e) {

						        var view = new DataView(e.target.result);
						        if (view.getUint16(0, false) != 0xFFD8) return callback(-2);
						        var length = view.byteLength;
						        var offset = 2;
						        while (offset < length) {
						            var marker = view.getUint16(offset, false);
						            offset += 2;
						            if (marker == 0xFFE1) {
						                var little = view.getUint16(offset += 8, false) == 0x4949;
						                offset += view.getUint32(offset + 4, little);
						                var tags = view.getUint16(offset, little);
						                offset += 2;
						                for (var i = 0; i < tags; i++)
						                    if (view.getUint16(offset + (i * 12), little) == 0x0112)
						                        return callback(view.getUint16(offset + (i * 12) + 8, little));
						            }
						            else if ((marker & 0xFF00) != 0xFF00) break;
						            else offset += view.getUint16(offset, false);
						        }
						        return callback(-1);
						    };
						    reader.readAsArrayBuffer(file.slice(0, 64 * 1024));
						}
						
						scope.replaceImage = function(domDiv, file){
							
							scope.getOrientation(file, function(orientation) {
								var reader = new FileReader();
							     //TODO resize
							     var image  = new Image();
							     reader.onload = function(_file) {
							    	 console.log(_file);
							    	 
							    	
									    
							         image.src    = this.result;              // url.createObjectURL(file);
							         image.onload = function() {
							             var w = this.width,
							                 h = this.height,
							                 t = file.type,                           // ext only: // file.type.split('/')[1],
							                 n = file.name,
							                 s = ~~(file.size/1024) +'KB';
							             var ratio = 1;
							             if (w > h) {
							            	 if (w > 800) {
							            		 ratio = w / 800;
							            	 }
							             } else {
							            	 if (h > 800) {
							            		 ratio = h / 800;						            		 
							            	 }
							             }
							             							             
							             var canvas = document.createElement('canvas');
								    	 
								    	 canvas.id     = "hiddenCanvas";
										 var sw = canvas.width  = w/ratio;
										 var sh = canvas.height = h/ratio;
										 canvas.style.visibility   = "hidden";  
										 document.body.appendChild(canvas);
//										 if (ratio == 1) return;
										 
										 var ctx = context = canvas.getContext('2d');
										 
										 context.clearRect(0,0,sw,sh);

								         var width = canvas.width,
								            height = canvas.height;
								         console.log("orientation : "+orientation)
								         if (orientation > 4) {
								             canvas.width = height;
								             canvas.height = width;
								         }
								         switch (orientation) {
								         case 2:
								             // horizontal flip
								             ctx.translate(width, 0);
								             ctx.scale(-1, 1);
								             break;
								         case 3:
								             // 180° rotate left
								             ctx.translate(width, height);
								             ctx.rotate(Math.PI);
								             break;
								         case 4:
								             // vertical flip
								             ctx.translate(0, height);
								             ctx.scale(1, -1);
								             break;
								         case 5:
								             // vertical flip + 90 rotate right
								             ctx.rotate(0.5 * Math.PI);
								             ctx.scale(1, -1);
								             break;
								         case 6:
								             // 90° rotate right
								             ctx.rotate(0.5 * Math.PI);
								             ctx.translate(0, -height);
								             break;
								         case 7:
								             // horizontal flip + 90 rotate right
								             ctx.rotate(0.5 * Math.PI);
								             ctx.translate(width, -height);
								             ctx.scale(-1, 1);
								             break;
								         case 8:
								             // 90° rotate left
								        	 console.log("rotate 90deg")
								             ctx.rotate(-0.5 * Math.PI);
								             ctx.translate(-width, 0);
								             break;
								         }

								         context.drawImage(image, 0, 0, this.width, this.height, 0, 0, sw, sh);
								         
 							             scope.fileToUpload = dataURLtoBlob(canvas.toDataURL());
// 							             console.log(canvas.toDataURL());

 								    	 domDiv.setAttribute('src', canvas.toDataURL());
								         document.body.removeChild(canvas);
							         };
							         image.onerror= function() {
							             alert('Invalid file type: '+ file.type);
							         };      
							     };
							     

							     reader.readAsDataURL(file);
								
								
							});
							
							 
//						     scope.resize(file, 800,600,0.5);
						     
						     
						}
						
						scope.updateImage = function(){
							console.log("Update image");
							var first = null;
							first = scope.user.jsonCustomFiles[0];
							

							console.log("Update image : "+first);
							if (first != null) {
								var imgDrop = element.find('#imageContainer')[0];//document.querySelector('#imageContainer0');
								imgDrop.setAttribute('src', rootPath+"rest/user/download/file/"+scope.user.id+"/"+first.key);//style.backgroundImage = "url('"+rootPath+"rest/user/download/file/"+$scope.adminUser.id+"/"+first.key+"')";
								var imgDrop = document.querySelector('#imageContainer');
								imgDrop.setAttribute('src', rootPath+"rest/user/download/file/"+scope.user.id+"/"+first.key);//style.backgroundImage = "url('"+rootPath+"rest/user/download/file/"+$scope.adminUser.id+"/"+first.key+"')";
							}
						}
						
						scope.uploadFile = function(fileToUp) {
							  var deferred = $q.defer();
							  			  
							  var getProgressListener = function(deferred) {
							    return function(event) {
							      var progress = $('#upLoadProgress');
							      progress.css({'visibility':'visible'});
							      progress.progress({percent:event.loaded*100/event.total});
							      if (event.loaded == event.total) {
							    	  $('.upload.large.icon.teal')
									  .transition('fly down');
							      }
							    };
							  };
							  var formData = new FormData();

							  formData.append("file", fileToUp);

							  $.ajax({
							    type: 'POST',
							    url:  rootPath + "rest/user/upload/file/"+scope.user.id,
							    data: formData,
							    cache: false,
							    // Force this to be read from FormData
							    contentType: false,
							    processData: false,
							    success: function(response, textStatus, jqXHR) {
							      deferred.resolve(response);
							      scope.user.jsonCustomFiles[0] = response;
							      var imgDrop1 = document.querySelector('#imageContainer0');
								  var imgDrop2 = document.querySelector('#imageContainer');
								  imgDrop1.setAttribute('src', imgDrop2.getAttribute('src'));
								  scope.addImageError('Upload success',0);
								  scope.fileToUpload = null;
								  scope.$apply();
							    },
							    error: function(jqXHR, textStatus, errorThrown) {
							      deferred.reject(errorThrown);
							    },
							    xhr: function() {
							      var myXhr = $.ajaxSettings.xhr();
							      if (myXhr.upload) {
							         myXhr.upload.addEventListener(
							            'progress', getProgressListener(deferred), false);
							      } else {
							        $log.log('Upload progress is not supported.');
							      }
							      return myXhr;
							    }
							  });
							  return deferred.promise;
							};
						
						
						scope.fileToUpload = null;

						
						scope.getImageFromUrl = function(){
							
						}
						
						var imageDrop = element[0].querySelector('#imageDrop');
						
						imageDrop.addEventListener('dragover', function (e) {			
							if (e.preventDefault) e.preventDefault(); 
		   					e.dataTransfer.dropEffect = 'copy';
		   					return false;
						});
						
						imageDrop.addEventListener( 'drop', function (e) {
							if (e.stopPropagation) e.stopPropagation();
							e.preventDefault();
//							console.log(e.dataTransfer.getData("text/uri-list"));
							console.log(e.dataTransfer.types);
							console.log(e.dataTransfer.getData("text/html"));
							var files = e.target.files || e.dataTransfer.files;
							var dropSuccess = false;
							var lastFile = null;
							
							for (var i = 0, f; f = files[i]; i++) {
								console.log("Name : "+f.name);
								console.log("type : "+f.type);
								console.log("size : "+f.size);
								
								lastFile = f;
								if (f.type.indexOf('image/') < 0 /*|| f.size > 1048576*/) continue;
								dropSuccess = true;
								$(this).transition('jiggle');
								var imgDrop2 = document.querySelector('#imageContainer');
								scope.replaceImage(imgDrop2, f);
								scope.fileToUpload = f;
								break;
							}
							
							if (!dropSuccess) {
								if (lastFile!= null) {
									if (lastFile.type.indexOf('image/') < 0 )
										scope.addImageError('File type invalid',2);
									if (lastFile.size > 1048576 )
										scope.addImageError('File Size invalid',2);
								} else {
									var fileUrl = e.dataTransfer.getData("text/uri-list");
									if (fileUrl != null) {

									} else
										scope.addImageError('No file detected',2);
									
									
									
								}
								return false;
							}
							if (files.length > 1) {
								scope.addImageError('Only 1 file is allowed',1);				
							}
							//$scope.uploadFile($scope.fileToUpload);
							var cloudUpload = $('.upload.large.icon.teal');
							if (cloudUpload.hasClass('hidden'))
								cloudUpload.transition('fade');
							$('#fileUploadId')[0].files[0] = scope.fileToUpload;
							$('#fileUploadId').attr({ value: '' });
							 var progress = $('#upLoadProgress');
								progress.css({'visibility':'visible'});
								progress.progress({percent:0});
							scope.$apply();
							return false;
						});
						
						scope.hasAddedUploadDialogListener = false;
						
						scope.uploadCameraDialog = function($event) {
							
						}
						
						scope.uploadDialog = function(){
							
							$('#fileUploadId').trigger('click');
							if (scope.hasAddedUploadDialogListener) return;
							$('#fileUploadId').change(function(){
								var f = $(this)[0].files[0];
								scope.fileToUpload =  f;

								var cloudUpload = $('.upload.large.icon.teal');
								if (cloudUpload.hasClass('hidden'))
									cloudUpload.transition('fade');
								$('#imageDrop').transition('jiggle');
								
								scope.replaceImage(document.querySelector('#imageContainer'), scope.fileToUpload);
								 var progress = $('#upLoadProgress');
									progress.css({'visibility':'visible'});
									progress.progress({percent:0});
								scope.$apply();
							});
							scope.hasAddedUploadDialogListener = true;
							
						}
						scope.updateImage();
					}
				};
			}
		}