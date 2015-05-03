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
