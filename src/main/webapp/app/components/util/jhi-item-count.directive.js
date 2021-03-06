(function () {
    'use strict';

    var jhiItemCount = {
        template: '<div><small class="text-muted m-t-sm m-b-sm">showing {{(($ctrl.page - 1) * $ctrl.itemsPerPage) == 0 ? 1 : (($ctrl.page - 1) * $ctrl.itemsPerPage + 1)}} - ' +
        '{{($ctrl.page * $ctrl.itemsPerPage) < $ctrl.queryCount ? ($ctrl.page * $ctrl.itemsPerPage) : $ctrl.queryCount}} of {{$ctrl.queryCount}} items</small></div>',
        bindings: {
            page: '<',
            queryCount: '<total',
            itemsPerPage: '<'
        }
    };

    angular
        .module('tourologistApp')
        .component('jhiItemCount', jhiItemCount);
})();

