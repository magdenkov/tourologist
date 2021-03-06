(function () {
    'use strict';

    angular
        .module('tourologistApp', [
            'ngStorage',
            'tmh.dynamicLocale',
            'pascalprecht.translate',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            'ngAnimate',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar',
            'uiGmapgoogle-maps',
            'angularGrid',
            'ngMapAutocomplete',
            '$q-spread',
            'checklist-model',
            'ui.select',
            'ui.sortable',
            'nya.bootstrap.select',

            'tourologistApp.dataservices',
            'tourologistApp.directives',
            'tourologistApp.services',
            'tourologistApp.tour',
            'tourologistApp.diy-tour'

        ])
        .run(run);

    run.$inject = ['stateHandler', 'translationHandler'];

    function run(stateHandler, translationHandler) {
        stateHandler.initialize();
        translationHandler.initialize();
    }
})();
