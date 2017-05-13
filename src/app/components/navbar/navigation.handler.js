angular
  .module('olApp')
  .factory('NavigationHandler', NavigationHandler);

function NavigationHandler($location) {
  var handler = {};
  handler.guestRoot = '/welcome';
  handler.loggedInRoot = '/books';
  handler.root = '/';


  handler.handleNavigationToRoot = function (nextLocation, session) {
    if (nextLocation.url == handler.root) {
      $location.path(session.authenticated ? handler.loggedInRoot : handler.guestRoot);
    }
  };

  handler.handleNavigationToProtectedPages = function (nextLocation, session) {
    if (nextLocation.role && !session.authenticated) {
      $location.path(handler.guestRoot);
    } else if (session.authenticated && session.hasUserRole && !session.hasUserRole(nextLocation.role)) {
      $location.path(handler.loggedInRoot);
    }
  };

  handler.handleNavigationToGuestPagesWhenLoggedIn = function (nextLocation, session) {
    if (!nextLocation.role && session.authenticated) {
      $location.path(handler.loggedInRoot);
    }
  };

  return handler;
}
