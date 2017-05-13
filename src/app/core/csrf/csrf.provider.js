angular
  .module('olApp')
  .provider('CsrfProvider',[function(){
    var headerName = 'X-XSRF-TOKEN';
    var cookieName = 'XSRF-TOKEN';
    var allowedMethods = ['GET', 'OPTIONS'];

    this.$get = ['$cookies', function($cookies){
      return {
        'request': function(config) {
          if(allowedMethods.indexOf(config.method) === -1) {
            config.headers[headerName] = $cookies.get(cookieName);
          }
          return config;
        }
      }
    }];
  }]).config(function($httpProvider) {
  $httpProvider.interceptors.push('CsrfProvider');
});
