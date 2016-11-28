/**
 * Created by rafael on 28/11/2016.
 */
angular
    .module('tourologistApp').factory('Mutator', function () {
    function Mutator(mutation) {
        this.mutation = mutation;
    }

    Mutator.prototype.transform = function (field) {
        return this.mutation(field);
    };

    return Mutator;
});
