(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TlLanguageController', TlLanguageController);

    TlLanguageController.$inject = ['$translate', 'TlLanguageService', 'tmhDynamicLocale'];

    function TlLanguageController ($translate, TlLanguageService, tmhDynamicLocale) {
        var vm = this;

        vm.changeLanguage = changeLanguage;
        vm.languages = null;

        TlLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });

        function changeLanguage (languageKey) {
            $translate.use(languageKey);
            tmhDynamicLocale.set(languageKey);
        }
    }
})();
