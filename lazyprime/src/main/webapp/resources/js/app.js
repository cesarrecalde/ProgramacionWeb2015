var app = angular.module('myApp', ['ngRoute'])



    .config(['$httpProvider', function($httpProvider) {
        $httpProvider.defaults.useXDomain = true;
        delete $httpProvider.defaults.headers.common['X-Requested-With'];
    }
    ])



    .controller('elementsController', function($scope, $http) {
        //Token for Authorization
        $http.defaults.headers.common.Authorization = 'Bearer 096fa935862e4c55db73e8f153ef867f'
        $scope.filter = {};
        $scope.tables = [];
        $scope.options = [];
        $scope.pagination = [];


        $scope.request = function(caseRequest){
            $scope.tables = [];
            switch (caseRequest) {
                case "loadPage":
                    $scope.progress = 0;
                    $http.get("http://localhost:8080/lazyprime/rest/providers")
                        .success(function(response) {
                            $scope.progress = $scope.progress + 20;
                            $scope.providers = response;
                        }).error(function(response){
                            $scope.progress = $scope.progress + 20;
                        })
                    $http.get("http://localhost:8080/lazyprime/rest/products")
                        .success(function(response) {
                            $scope.progress = $scope.progress + 20;
                            $scope.products = response;
                        }).error(function(response){
                            $scope.progress = $scope.progress + 20;
                        })
                    $http.get("http://localhost:8080/lazyprime/rest/comprasDetalles")
                        .success(function(response) {
                            $scope.progress = $scope.progress + 10;
                            $scope.listaDePedidoCompra = response;
                        }).error(function(response){
                            $scope.progress = $scope.progress + 10;
                        })
                    $http.get("http://localhost:8080/lazyprime/rest/compras")
                        .success(function(response) {
                            $scope.progress = $scope.progress + 10;
                            $scope.listaDeCompras = response;
                        }).error(function(response){
                            $scope.progress = $scope.progress + 10;
                        })
                    $http.get("http://localhost:8080/lazyprime/rest/clients")
                        .success(function(response) {
                            $scope.progress = $scope.progress + 20;
                            $scope.listaDeClientes = response;
                        }).error(function(response){
                            $scope.progress = $scope.progress + 20;
                        })
                    $http.get("http://localhost:8080/lazyprime/rest/ventas")
                        .success(function(response) {
                            $scope.progress = $scope.progress + 10;
                            $scope.listaDeVentas = response;
                        }).error(function(response){
                            $scope.progress = $scope.progress + 10;
                        })
                    $http.get("http://localhost:8080/lazyprime/rest/ventasDetalles")
                        .success(function(response) {
                            $scope.progress = $scope.progress + 10;
                            $scope.listaDePedidoVenta = response;
                        }).error(function(response){
                            $scope.progress = $scope.progress + 10;
                        })

                    break;
            }
        }


        //Funciones para la paginacion remota
        $scope.pagination.main = {
            page: 1,
            take: 15
        };

        $scope.pagination.nextPage = function() {
            if ($scope.pagination.main.page < $scope.metas.total_pages) {
                $scope.pagination.main.page++;
                $scope.request('loadPage');
            }
        };

        $scope.pagination.previousPage = function() {
            if ($scope.pagination.main.page > 1) {
                $scope.pagination.main.page--;
                $scope.request('loadPage');
            }
        };


        // Funciones de registro

        $scope.registrarProveedor = function () {

            $http({
                url: 'http://localhost:8080/lazyprime/rest/providers',
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                data: {name: $scope.proveedor.name}
            }).success(function() {
                $scope.request('loadPage');
                $scope.proveedor.name = '';
            })
        }

        $scope.registrarCliente = function () {

            $http({
                url: 'http://localhost:8080/lazyprime/rest/clients',
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                data: {name: $scope.cliente.name}
            }).success(function() {
                $scope.request('loadPage');
                $scope.cliente.name = '';

            })
        }

        $scope.agregarACarritoDeCompra = function(producto,cantidadSolicitada){
            $http({
                url: 'http://localhost:8080/lazyprime/rest/comprasDetalles',
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                data: {product:producto , cantidad:cantidadSolicitada}
            }).success(function() {
                $scope.request('loadPage');
            })
        }

        $scope.agregarACarritoDeVenta = function(producto,cantidadSolicitada){
            $http({
                url: 'http://localhost:8080/lazyprime/rest/ventasDetalles',
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                data: {product:producto , cantidad:cantidadSolicitada}
            }).success(function() {
                $scope.request('loadPage');
            })
        }

        $scope.registrarProducto = function () {
            $http({
                url: 'http://localhost:8080/lazyprime/rest/products',
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                data: {nameProduct: $scope.producto.nameProduct, precioUnitario: $scope.producto.precioUnitario, cantidad:$scope.producto.cantidad, product_provider:$scope.producto.product_provider_id}
            }).success(function() {
                $scope.producto.precioUnitario = 0;
                $scope.producto.nameProduct = "";
                $scope.producto.cantidad = 0;
                $scope.producto.product_provider_id = 0;
                $scope.request('loadPage');
            })
        }

        $scope.enviarArchivoProducto = function () {
            $scope.data;
            var f = document.getElementById('fileProducto').files[0],
                r = new FileReader();
            r.onloadend = function(e){
                $scope.data = e.target.result;
            }
            r.readAsBinaryString(f);
        }

        $scope.comprarProducto = function (producto) {
            console.log(producto);
        }

        $scope.compraPedido = function(listaDePedidos){
            $http({
                url: 'http://localhost:8080/lazyprime/rest/compras',
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                data: {compraDetalles:listaDePedidos}
            }).success(function(response) {
                $scope.request('loadPage');
            }).error(function (response) {
                alert(response.error);
            })
        }

        $scope.ventaPedido = function(listaDePedidos){
            $http({
                url: 'http://localhost:8080/lazyprime/rest/ventas',
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                data: {ventaDetalles:listaDePedidos}
            }).success(function(response) {
                $scope.request('loadPage');
            }).error(function (response) {
                alert(response.error);
            })
        }




        // Funciones de eliminacion

        $scope.eliminarProductoDeListaDeCompras = function (id) {
            $http.delete("http://localhost:8080/lazyprime/rest/comprasDetalles/" + id).success(function() {
                $scope.request('loadPage');
            })
        }

        $scope.eliminarProductoDeListaDeVentas = function (id) {
            $http.delete("http://localhost:8080/lazyprime/rest/ventasDetalles/" + id).success(function() {
                $scope.request('loadPage');
            })
        }

        $scope.eliminarProveedor = function (id) {
            $http.delete("http://localhost:8080/lazyprime/rest/providers/" + id).success(function() {
                $scope.request('loadPage');
            })
        }

        $scope.enviarArchivoProveedor = function () {
            $scope.data;
            var f = document.getElementById('fileProveedor').files[0],
                r = new FileReader();
                r.onloadend = function(e){
                    $scope.data = e.target.result;
                }
                r.readAsBinaryString(f);
                console.log($scope.data);

        }




        //Funciones de listado

        //Lista inicial
        $scope.request('loadPage');




    })