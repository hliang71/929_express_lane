angular.module('controllers', [])


  .controller('AppCtrl', function($scope, $ionicModal, $timeout) {

    // With the new view caching in Ionic, Controllers are only called
    // when they are recreated or on app start, instead of every page change.
    // To listen for when this page is active (for example, to refresh data),
    // listen for the $ionicView.enter event:
    //$scope.$on('$ionicView.enter', function(e) {
    //});

    // Form data for the login modal
    $scope.loginData = {};

    // Create the login modal that we will use later
    $ionicModal.fromTemplateUrl('templates/login.html', {
      scope: $scope
    }).then(function(modal) {
      $scope.modal = modal;
    });

    // Triggered in the login modal to close it
    $scope.closeLogin = function() {
      $scope.modal.hide();
    };

    // Open the login modal
    $scope.login = function() {
      $scope.modal.show();
    };

    // Perform the login action when the user submits the login form
    $scope.doLogin = function() {
      console.log('Doing login', $scope.loginData);

      // Simulate a login delay. Remove this and replace with your login
      // code if using a login system
      $timeout(function() {
        $scope.closeLogin();
      }, 1000);
    };

  })


  .controller('LoginCtrl', function($scope) {
    $scope.doLogin = function() {
      console.log('logging in');
    }
  })

  .controller('ProfileCtrl', function($scope) {

  })

  .controller('SettingsCtrl', function($scope) {

  })

  .controller('HomeCtrl', function($scope) {
    $scope.playlists = [
      { title: 'Parking', id: 1 },
      { title: 'Shopping', id: 2 },
      { title: 'Game', id: 3 },
      { title: 'Movie', id: 4 },
      { title: 'Social Life', id: 5 },
      { title: 'Leaning', id: 6 }
    ];
  })

  .controller('MapCtrl', function($scope, $ionicLoading, $compile) {

     var stompClient = null;

     var setConnected = function(connected) {
       document.getElementById('connect').disabled = connected;
       document.getElementById('disconnect').disabled = !connected;
       document.getElementById('calculationDiv').style.visibility = connected ? 'visible' : 'hidden';
       document.getElementById('calResponse').innerHTML = '';
     };

     var connect = function() {
       var socket = new SockJS('http://localhost:8080/notification/add');
       stompClient = Stomp.over(socket);
       stompClient.connect({}, function(frame) {
         //setConnected(true);
         console.log('Connected: ' + frame);
         stompClient.subscribe('/topic/showResult', function(calResult) {
           console.log(JSON.stringify(calResult.body))
         });
       });
     };

     var disconnect = function() {
       stompClient.disconnect();
       setConnected(false);
       console.log("Disconnected");
     };

    /*-----------------------------------------
     */

    connect();

    var map_options = {
      center: new google.maps.LatLng(37.78621,-122.4209302),
      zoom: 14,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    $scope.markers = [];
    var google_map = new google.maps.Map(document.getElementById("map"), map_options);

    var info_window = new google.maps.InfoWindow({
      content: 'loading'
    });

    var x = 37.78721;
    var y = -122.4109302;
    $scope.addMarker = function() {
      console.log('in the function addMarker');
      var json = {
        id:123,
        x: x,
        y: y,
        title: 'Location Name 2',

      };
      var m = new google.maps.Marker({
        id: json.id,
        map:       google_map,
        animation: google.maps.Animation.bouce,
        title:     'Location Name 2',
        position:  new google.maps.LatLng(json.x, json.y),
        html:      '<p><strong>Parking Location 4</strong><br/>Address 4</p>'
      });
      google.maps.event.addListener(m, 'click', function() {
        info_window.setContent(this.html);
        info_window.open(google_map, this);
      });
      $scope.markers.push(m);
      x = x + 0.001;
      y = y + 0.001;
    };

    var setAllMap = function(map) {
      for (var i = 0; i < $scope.markers.length; i++) {
        $scope.markers[i].setMap(map);
      }
    };

    $scope.removeMarker = function() {
      console.log('removing markers');
      $scope.markers[0].setMap(null);
      $scope.markers.splice(0,1);
    };

    window.scope = $scope;

  });
