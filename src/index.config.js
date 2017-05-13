angular
  .module('olApp')
  .config(configLogProvider)
  .config(configHttpProvider)
  .config(configToastr)
  .config(configLocationProvider);

function configLogProvider($logProvider) {
  $logProvider.debugEnabled(true);
}

function configHttpProvider($httpProvider) {
  $httpProvider.defaults.withCredentials = true;
  $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
}

function configToastr(toastrConfig) {
  toastrConfig.allowHtml = true;
  toastrConfig.timeOut = 2500;
  toastrConfig.positionClass = 'toast-top-right';
  toastrConfig.preventDuplicates = false;
  toastrConfig.progressBar = false;
  toastrConfig.tapToDismiss = true;
}

function configLocationProvider($locationProvider) {
  $locationProvider.html5Mode(true);
}
