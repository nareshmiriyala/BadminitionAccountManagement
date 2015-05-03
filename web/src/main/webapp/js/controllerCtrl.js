/**
 * @ngdoc controller
 * @name controller
 *
 * @description
 * _Please update the description and dependencies._
 *
 * @requires $scope
 * */
angular.module('registerApp',[])
    .controller('RegController',['$http', function($scope,$http){

      $scope.player={};
        $scope.submit=function(){
          console.log("called method");
          $http.jsonp('/web/register',$scope.player).success(function (data)
          {
              $scope.status=data;
          });
      };
}]);
