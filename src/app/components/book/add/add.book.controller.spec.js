describe('add book controller', function () {
  var ctrl, $scope, $rootScope, BookService, $uibModalInstance,
    defer, $window, $translate;

  module.sharedInjector();
  beforeAll(module('olApp'));
  beforeAll(inject(function (_$rootScope_, _$controller_, _$window_, _$q_) {
    $rootScope = _$rootScope_;
    $scope = _$rootScope_.$new();
    BookService = jasmine.createSpyObj('service', ['upload']);
    $uibModalInstance = jasmine.createSpyObj('modal', ['dismiss']);
    defer = _$q_.defer();
    $window = {};
    $translate = jasmine.createSpy('$translate').and.returnValue(defer.promise);
    ctrl = _$controller_('AddBookController', {
      $scope : $scope,
      $rootScope: $rootScope,
      BookService: BookService,
      $uibModalInstance: $uibModalInstance,
      $window: $window,
      $translate: $translate
    });
  }));

  it ('should have controller and initial values defined', function () {
    expect(ctrl).toBeDefined();
    expect(ctrl.required).toBeFalsy();
    expect(ctrl.error).toBeFalsy();
    expect(ctrl.error).toBeFalsy();
  });

  it ('should dismiss modal window on cancel', function () {
    ctrl.closeModal();
    expect($uibModalInstance.dismiss).toHaveBeenCalledWith('cancel');
  });

  it('should indicate required when some data is unavailable', function () {
    $scope.form = {$valid: false};
    ctrl.upload();
    expect(BookService.upload).not.toHaveBeenCalled();
    expect(ctrl.required).toBeTruthy();
  });

  it('should upload when all data available', function () {
    $scope.form = {$valid: true};
    ctrl.file = {};
    ctrl.upload();
    expect(BookService.upload).toHaveBeenCalled();
  });

  it('should indicate success on well formed upload', function () {
    response = {status: 200};
    $scope.form = {
      $setPristine: function () {},
      $setUntouched: function () {}
    };
    $window.document = {
      getElementsByName: function () {
        return [{reset: function () {}}];
      }
    };
    spyOn($scope.form, '$setPristine');
    spyOn($scope.form, '$setUntouched');
    ctrl.onUpload(response);
    expect(ctrl.success).toBeTruthy();
    expect(ctrl.error).toBeFalsy();
    expect(ctrl.book).toEqual(null);
    expect(ctrl.file).toEqual(null);
    expect($scope.form.$setPristine).toHaveBeenCalled();
    expect($scope.form.$setUntouched).toHaveBeenCalled();
    });

  it('should indicate error on wrong upload', function () {
    response = {status: 400};
    ctrl.onUpload(response);
    expect(ctrl.error).toBeTruthy();
    expect(ctrl.success).toBeFalsy();
  });

  it('should indicate error on wrong upload', function () {
    response = {status: 500};
    ctrl.onUpload(response);
    expect(ctrl.error).toBeTruthy();
    expect(ctrl.success).toBeFalsy();
  });

  it('should call translate function on event', function () {
    spyOn(ctrl, 'translate');
    $rootScope.$broadcast('$translateChangeSuccess');
    expect(ctrl.translate).toHaveBeenCalled();
  });

  it('should call $translate service to get translations', function () {
    ctrl.translate();
    expect($translate).toHaveBeenCalled();
  });

});
