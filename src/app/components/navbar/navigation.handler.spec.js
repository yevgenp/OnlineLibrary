describe('navigation handler', function () {
  var NavigationHandler, $location;

  module.sharedInjector();
  beforeAll(module('olApp'));
  beforeAll(inject(function (_NavigationHandler_, _$location_) {
    NavigationHandler = _NavigationHandler_;
    $location = _$location_;
  }));

  beforeEach(function () {
    spyOn($location, 'path');
  });

  it('should initialize properly', function () {
    expect(NavigationHandler).toBeDefined();
    expect(NavigationHandler.root).toEqual('/');
    expect(NavigationHandler.guestRoot).toEqual('/welcome');
    expect(NavigationHandler.loggedInRoot).toEqual('/books');
  });

  it('should redirect to /welcome if guest goes to /', function () {
    var nextLocation = {url: '/'};
    var session = {authenticated: false};
    NavigationHandler.handleNavigationToRoot(nextLocation, session);
    expect($location.path).toHaveBeenCalledWith(NavigationHandler.guestRoot);
  });

  it('should redirect to /books if logged user goes to /', function () {
    var nextLocation = {url: '/'};
    var session = {authenticated: true};
    NavigationHandler.handleNavigationToRoot(nextLocation, session);
    expect($location.path).toHaveBeenCalledWith(NavigationHandler.loggedInRoot);
  });

  it('should redirect to /welcome if user goes to protected page without logging in', function () {
    var nextLocation = {url: '/users', role: 'USER'};
    var session = {authenticated: false};
    NavigationHandler.handleNavigationToProtectedPages(nextLocation, session);
    expect($location.path).toHaveBeenCalledWith(NavigationHandler.guestRoot);
  });

  it('should redirect to /home if user goes to protected page without specified role', function () {
    var nextLocation = {url: '/users', role: 'ADMINISTRATOR'};
    var session = {authenticated: true, roles: ['USER']};
    setUserRoleFunction(session);
    NavigationHandler.handleNavigationToProtectedPages(nextLocation, session);
    expect($location.path).toHaveBeenCalledWith(NavigationHandler.loggedInRoot);
  });

  it('should allow user to see page he has role for', function () {
    var nextLocation = {url: '/books', role: 'USER'};
    var session = {authenticated: true, roles: ['USER']};
    setUserRoleFunction(session);
    NavigationHandler.handleNavigationToProtectedPages(nextLocation, session);
    expect($location.path).not.toHaveBeenCalledWith('/home');
    expect($location.path).not.toHaveBeenCalledWith('/welcome');
  });

  it('should redirect to /books if logged user goes to guest page', function () {
    var nextLocation = {url: '/login', protected: false};
    var session = {authenticated: true};
    NavigationHandler.handleNavigationToGuestPagesWhenLoggedIn(nextLocation, session);
    expect($location.path).toHaveBeenCalledWith(NavigationHandler.loggedInRoot);
  });

  function setUserRoleFunction(session) {
    session.hasUserRole = function (userRole) {
      var answer = false;
      this.roles.forEach(function (role) {
        if (role == userRole) {
          answer = true;
        }
      });
      return answer;
    };
  }

});
