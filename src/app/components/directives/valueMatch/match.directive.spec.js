describe('match directive', function() {
  var $scope, element, form;

  beforeEach(module('olApp'));

  beforeEach(inject(function($compile, $rootScope) {
    $scope = $rootScope;
    element = angular.element(
      '<form name="form">' +
      '<input name="password" ng-model="password"/>' +
      '<input name="passwordConfirm" ' +
      'ng-model="passwordConfirm" value-match="password"/>' +
      '</form>'
    );
    $compile(element)($scope);
    $scope.$digest();
    form = $scope.form;
  }));

  it('should have error when inputs don\'t match', function () {
    form.password.$setViewValue('12345');
    form.passwordConfirm.$setViewValue('1234');
    $scope.$digest();
    expect(form.passwordConfirm.$error.valueMatch).toBeTruthy();
  });
});

