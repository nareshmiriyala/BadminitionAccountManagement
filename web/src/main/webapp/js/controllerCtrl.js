angular.module('registerApp',[])
    .controller('RegController',function($scope,$http){
      $scope.player={};
        $scope.submit=function(){
          console.log("called method");
          $http.jsonp('http://localhost:8080/web/register',$scope.player);
      };
});
