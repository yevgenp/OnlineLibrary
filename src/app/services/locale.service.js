'use strict';

angular
  .module('olApp')
  .factory('LocaleService', LocaleService);

function LocaleService($http, backend, $translate) {
  var service = {};
  service.languages = [
    {id: "en", title: "English", icon: "gb"},
    {id: "uk", title: "Ukrainian", icon: "ua"}
  ];

  service.initLanguages = function () {
    var currentLocale = $translate.use();
    var savedLanguage = findLanguageById(currentLocale);
    service.activateLanguage(savedLanguage);
  };

  var findLanguageById = function (id) {
    for (var i = 0; i < service.languages.length; i++) {
      if (service.languages[i].id === id) return service.languages[i];
    }
    return null;
  };

  service.activateLanguage = function (language) {
    if (language) {
      $translate.use(language.id);
      var index = service.languages.indexOf(language);
      var chosen = service.languages.splice(index, 1);
      service.languages.unshift(chosen[0]);
      $http.get(backend + 'api/authentication/login?locale=' + language.id);
    }
  };

  return service;
}
