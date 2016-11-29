/**
 * Created by rafael on 29/11/2016.
 */

(function () {
    'use strict';

    angular
        .module('tourologistApp')
        .directive('fileread', fileread);

    fileread.$inject = ['$parse'];

    function fileread($parse) {
        var directive = {
            scope: {
                fileread: "=",
                filedata: "="
            },
            link: function (scope, element, attributes) {
                element.bind("change", function (changeEvent) {
                    var reader = new FileReader();

                    reader.onload = function (loadEvent) {
                        scope.$apply(function () {
                            scope.fileread = changeEvent.target.files[0];
                            scope.filedata = loadEvent.target.result;
                        });
                    }

                    reader.readAsDataURL(changeEvent.target.files[0]);
                });
            }
        };

        return directive;
    }


})();
