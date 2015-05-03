angular.module('registerApp',[])
    .controller('RegController',function($scope,$http){
      $scope.player={};
        $scope.submit=function(){
          console.log("called method");
            $http({
                method: 'POST',
                url: 'http://localhost:8080/web/register',
                headers: {'Content-Type': 'application/json'},
                data:  $scope.player
            });
      };
});
