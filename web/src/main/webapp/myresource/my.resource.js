var module = angular.module( 'my.resource', [ 'ngResource' ] );

module.factory( 'Resource', [ '$resource', function( $resource ) {
   return function( url, params, methods ) {
     var defaults = {
        update: { method: 'PUT', isArray: false },
        create: { method: 'POST' }
     };
      
     methods = angular.extend( defaults, methods );
 
     var resource = $resource( url, params, methods );
 
     resource.prototype.$save = function(success, error) {
       if ( this.uuid === undefined ) {
         return this.$create({}, success, error);
       }
       else {
         return this.$update({}, success, error);
       }
     };
 
     return resource;
   };
}]);