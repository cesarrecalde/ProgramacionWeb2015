var app = angular.module('myApp', ['ngRoute'])

    .config(function($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'tableContent.html'
            })
            .otherwise({
                redirectTo: '/'
            });
    })

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

        //Select for Order ascend or descendt
        $scope.options.dataOrderAscOrDesc = {
            availableOptions: [
                {id: '1', value: 'asc', name: 'Ordenamiento Ascendente'},
                {id: '2', value: 'desc', name: 'Ordenamiento Descendente'}
            ],
            selectedOptionOrden: {id: '1', name: 'Ordenamiento Ascendente'}
        };

        //Select for order by
        $scope.options.dataOrderBy = {
            availableOptions: [
                {id: '1', value: 'fecha', name: 'Ordenar por Fecha'},
                {id: '2', value: 'monto_total', name: 'Ordenar por Monto'},
                {id: '3', value: 'numero', name: 'Ordenar por Numero'},
                {id: '4', value: 'ruc_cliente', name: 'Ordenar por Ruc'},
                {id: '5', value: 'nombre_cliente', name: 'Ordenar por Cliente'}
            ],
            selectedOption: {id: '1',name: 'Ordenar por Fecha'}
        };


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
                            $scope.progress = $scope.progress + 20;
                            $scope.listaDePedido = response;
                        }).error(function(response){
                            $scope.progress = $scope.progress + 20;
                        })
                    $http.get("http://localhost:8080/lazyprime/rest/compras")
                        .success(function(response) {
                            $scope.progress = $scope.progress + 40;
                            $scope.listaDeCompras = response;
                        }).error(function(response){
                            $scope.progress = $scope.progress + 40;
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

        //Funcion para desplegar mensaje al seleccionar una tupla
        $scope.show = function($producto){
            alert("Elemento seleccionado\nNumero: "+$producto.numero+"\nMonto total: "+$producto.monto_total+"\nNombre del cliente: "+$producto.nombre_cliente+"\nRuc del cliente: "+$producto.ruc_cliente+"\nFecha: "+$producto.fecha   );
        };

        $scope.registrarProveedor = function () {

            $http({
                url: 'http://localhost:8080/lazyprime/rest/providers',
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                data: {name: $scope.proveedor.name}
            }).success(function() {
                $scope.request('loadPage');
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


        $scope.eliminarProductoDeListaDeCompras = function (id) {
            $http.delete("http://localhost:8080/lazyprime/rest/comprasDetalles/" + id).success(function() {
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

        $scope.comprarPedido = function(listaDePedidos){
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

        //Lista inicial
        $scope.request('loadPage');

    })