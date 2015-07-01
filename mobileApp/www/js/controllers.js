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

    var t = [];
    var x = [];
    var y = [];
    var h = [];

    t.push('Location Name 1');
    x.push(37.78621);
    y.push(-122.4109302);
    h.push('<p><strong>Parking Location1</strong><br/>Address 1</p>');

    t.push('Location Name 2');
    x.push(37.7866848);
    y.push(-122.4094282);
    h.push('<p><strong>Parking Location 2</strong><br/>Address 2</p>');

    t.push('Location Name 2');
    x.push(37.7872275);
    y.push(-122.423883);
    h.push('<p><strong>Parking Location 3</strong><br/>Address 3</p>');

    t.push('Location Name 2');
    x.push(37.7830897);
    y.push(-122.4233251);
    h.push('<p><strong>Parking Location 4</strong><br/>Address 4</p>');

    var i = 0;
    for ( item in t ) {
      var m = new google.maps.Marker({
        map:       google_map,
        animation: google.maps.Animation.DROP,
        title:     t[i],
        position:  new google.maps.LatLng(x[i],y[i]),
        html:      h[i]
      });
      $scope.markers.push(m);
      google.maps.event.addListener(m, 'click', function() {
        info_window.setContent(this.html);
        info_window.open(google_map, this);
      });
      i++;
    }

    var x = 37.78721;
    var y = -122.4109302;
    $scope.addMarker = function() {
      console.log('in the function addMarker');
      var m = new google.maps.Marker({
        map:       google_map,
        animation: google.maps.Animation.DROP,
        title:     'Location Name 2',
        position:  new google.maps.LatLng(x, y),
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
    }

    $scope.removeMarker = function() {
      setAllMap(null);
      $scope.markers = []
    };

    //google.maps.event.addDomListener(window, 'load', initialize());

    $scope.centerOnMe = function() {
      if(!$scope.map) {
        return;
      }

      $scope.loading = $ionicLoading.show({
        content: 'Getting current location...',
        showBackdrop: false
      });

      navigator.geolocation.getCurrentPosition(function(pos) {
        $scope.map.setCenter(new google.maps.LatLng(pos.coords.latitude, pos.coords.longitude));
        $scope.loading.hide();
      }, function(error) {
        alert('Unable to get location: ' + error.message);
      });
    };

    $scope.clickTest = function() {
      alert('Example of infowindow with ng-click')
    };

    window.scope = $scope;

  });
