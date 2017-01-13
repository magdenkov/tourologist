(function () {
    'use strict';

    angular
        .module('tourologistApp.directives')
        .directive('tourButterbar', ['$rootScope', '$anchorScroll', tourButterbar]);

    function tourButterbar($rootScope, $anchorScroll) {
        return {
            restrict: 'AC',
            template:'<span class="bar"></span>',
            link: function(scope, el, attrs) {
                el.addClass('butterbar hide');
                scope.$on('$stateChangeStart', function(event) {
                    $anchorScroll();
                    el.removeClass('hide').addClass('active');
                });
                scope.$on('$stateChangeSuccess', function( event, toState, toParams, fromState ) {
                    event.targetScope.$watch('$viewContentLoaded', function(){
                        el.addClass('hide').removeClass('active');
                    })
                });
            }
        };
    }

})();
