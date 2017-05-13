describe('main controller', function () {
  var ctrl,
    $controller, $timeout,
    $state, LoginService, NavigationHandler,
    $rootScope;

  module.sharedInjector();
  beforeAll(module('olApp'));
  beforeAll(inject(
    function (_$controller_, _$state_, _$timeout_, _LoginService_,
              _$rootScope_) {
      $controller = _$controller_;
      $state = {current: {}};
      $timeout = _$timeout_;
      LoginService = _LoginService_;
      $rootScope = _$rootScope_;
      createNavigationHandler();
      createController();
    }));

  function createNavigationHandler() {
    NavigationHandler = jasmine.createSpyObj('NavigationHandler', [
      'handleNavigationToRoot',
      'handleNavigationToProtectedPages',
      'handleNavigationToGuestPagesWhenLoggedIn',
    ]);
  }

  function createController() {
    ctrl = $controller('MainController', {
      NavigationHandler: NavigationHandler,
      $state: $state
    });
  }

  it('should initialize properly', function () {
    expect(ctrl).toBeDefined();
    expect(ctrl.viewProperties.loaded).toBeFalsy();
  });

  it('should show content on timeout', function () {
    ctrl.showContentWhenLoaded();
    $timeout.flush();
    expect(ctrl.viewProperties.loaded).toBeTruthy();
  });

  it('should delegate url changes to NavigationHandler', function () {
    ctrl.onAuthenticationCompleted();
    expect(NavigationHandler.handleNavigationToRoot).toHaveBeenCalled();
    expect(NavigationHandler.handleNavigationToProtectedPages).toHaveBeenCalled();
    expect(NavigationHandler.handleNavigationToGuestPagesWhenLoggedIn).toHaveBeenCalled();
  });

  it("should not call NavigationHandler if " +
     "next url location allows access to ANY role", function () {
    $state.current = {role: 'ANY'};
    createNavigationHandler();
    ctrl.onAuthenticationCompleted();
    expect(NavigationHandler.handleNavigationToRoot).not.toHaveBeenCalled();
    expect(NavigationHandler.handleNavigationToProtectedPages).not.toHaveBeenCalled();
    expect(NavigationHandler.handleNavigationToGuestPagesWhenLoggedIn).not.toHaveBeenCalled();
  });

  it('should call LoginService authenticate method to reestablish session', function () {
    spyOn(LoginService, 'authenticate');
    createController();
    expect(LoginService.authenticate).toHaveBeenCalled();
  });

});
