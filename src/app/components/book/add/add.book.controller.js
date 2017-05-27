angular
  .module('olApp')
  .controller('AddBookController', AddBookController);
function AddBookController ($scope, $rootScope, BookService, $uibModalInstance,
                            $window, $translate) {
  var ctrl = this;
  ctrl.translations = {};

  function resetNotifications() {
    ctrl.required = false;
    ctrl.error = false;
    ctrl.success = false;
  }

  resetNotifications();

  ctrl.closeModal = function() {
    $uibModalInstance.dismiss('cancel');
  };

  ctrl.upload = function () {
    if($scope.form.$valid && ctrl.file) {
      resetNotifications();
      BookService.upload(ctrl.book, ctrl.file, ctrl.onUpload);
    }
    else {
      ctrl.text = ctrl.translations['fileRequired'];
      ctrl.required = true;
    }
  };

  function resetForm() {
    ctrl.book = null;
    ctrl.file = null;
    $window.document.getElementsByName('form')[0].reset();
    $scope.form.$setPristine();
    $scope.form.$setUntouched();
  }

  ctrl.onUpload = function(response) {
    switch (response.status) {
      case 200:
        ctrl.error = false;
        ctrl.success = true;
        ctrl.text = ctrl.translations['addSuccess'];
        resetForm();
        $rootScope.$broadcast('books list updated');
        break;
      case 400:
        ctrl.success = false;
        ctrl.error = true;
        ctrl.text = ctrl.translations['error'];
        break;
      default:
        ctrl.success = false;
        ctrl.error = true;
        ctrl.text = ctrl.translations['unknownError'];
        console.log(response);
        break;
    }
  };

  $rootScope.$on('$translateChangeSuccess', function() {ctrl.translate()});

  ctrl.translate = function () {
    $translate(['book.fileRequired', 'book.addSuccess', 'book.error', 'common.unknownError'])
      .then(function (translations) {
        ctrl.translations['fileRequired'] = translations['book.fileRequired'];
        ctrl.translations['addSuccess'] = translations['book.addSuccess'];
        ctrl.translations['error'] = translations['book.error'];
        ctrl.translations['unknownError'] = translations['common.unknownError'];
      });
  };

  ctrl.translate();
}
