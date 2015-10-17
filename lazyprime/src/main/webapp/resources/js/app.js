var app = angular.module('myApp', ['ngRoute'])



    .config(['$httpProvider', function($httpProvider) {
        $httpProvider.defaults.useXDomain = true;
        delete $httpProvider.defaults.headers.common['X-Requested-With'];
    }
    ])



    .controller('elementsController', function($scope, $http, $sce) {
        //Token for Authorization
        $http.defaults.headers.common.Authorization = 'Bearer 096fa935862e4c55db73e8f153ef867f'
        $scope.filter = {};
        $scope.options = [];
        $scope.pagination = [];
        $scope.siguienteProveedor = 0;
        $scope.siguienteProducto = 0;
        $scope.siguienteCarritoCompra = 0;
        $scope.siguienteCompra = 0;
        $scope.siguienteCliente = 0;
        $scope.siguienteVenta = 0;
        $scope.siguienteCarritoVenta = 0;
        $scope.siguienteSolicitud = 0;
        $scope.orderByPriceProduct = '';


        $scope.request = function(caseRequest){
            switch (caseRequest) {
                case "loadPage":
                    $scope.progress = 0;
                    $http.get("http://localhost:8080/lazyprime/rest/providers/"+$scope.siguienteProveedor)
                        .success(function(response) {
                            $scope.progress = $scope.progress + 20;
                            $scope.providers = response;
                        }).error(function(response){
                            $scope.progress = $scope.progress + 20;
                        })
                    $http.get("http://localhost:8080/lazyprime/rest/products"+$scope.orderByPriceProduct+"/"+$scope.siguienteProducto)
                        .success(function(response) {
                            $scope.progress = $scope.progress + 20;
                            $scope.products = response;
                        }).error(function(response){
                            $scope.progress = $scope.progress + 20;
                        })
                    $http.get("http://localhost:8080/lazyprime/rest/compraDetalle/"+$scope.siguienteCarritoCompra)
                        .success(function(response) {
                            $scope.progress = $scope.progress + 10;
                            $scope.listaDePedidoCompra = response;
                        }).error(function(response){
                            $scope.progress = $scope.progress + 10;
                        })
                    $http.get("http://localhost:8080/lazyprime/rest/compras/"+$scope.siguienteCompra)
                        .success(function(response) {
                            $scope.progress = $scope.progress + 10;
                            $scope.listaDeCompras = response;
                        }).error(function(response){
                            $scope.progress = $scope.progress + 10;
                        })
                    $http.get("http://localhost:8080/lazyprime/rest/clients/"+$scope.siguienteCliente)
                        .success(function(response) {
                            $scope.progress = $scope.progress + 20;
                            $scope.listaDeClientes = response;
                        }).error(function(response){
                            $scope.progress = $scope.progress + 20;
                        })
                    $http.get("http://localhost:8080/lazyprime/rest/ventas/"+$scope.siguienteVenta)
                        .success(function(response) {
                            $scope.progress = $scope.progress + 10;
                            $scope.listaDeVentas = response;
                        }).error(function(response){
                            $scope.progress = $scope.progress + 10;
                        })
                    $http.get("http://localhost:8080/lazyprime/rest/ventasDetalles/"+$scope.siguienteCarritoVenta)
                        .success(function(response) {
                            $scope.progress = $scope.progress + 10;
                            $scope.listaDePedidoVenta = response;
                        }).error(function(response){
                            $scope.progress = $scope.progress + 10;
                        })
                    $http.get("http://localhost:8080/lazyprime/rest/solicitudes/"+$scope.siguienteSolicitud)
                        .success(function(response) {
                            $scope.progress = $scope.progress + 10;
                            $scope.solicitudes = response;
                        }).error(function(response){
                            $scope.progress = $scope.progress + 10;
                        })

                    break;
            }
        }


        //Funciones para la paginacion remota

        // Proveedor
        $scope.pagination.siguienteProveedor = function () {
            $scope.siguienteProveedor = $scope.siguienteProveedor + 5;
            $scope.request('loadPage');
        }


        // Ordenar por nombre de producto
        $scope.ordenarPorPrecioProducto = function(){
            console.log(">>>>>>>>");
            if($scope.orderByPriceProduct == ''){
                $scope.orderByPriceProduct = "/ordenByPrice";
            }else{
                $scope.orderByPriceProduct = '';
            }

            console.log(">>>>>> ".$scope.orderByPriceProduct);
            $scope.request('loadPage');
        }

        $scope.pagination.anteriorProveedor = function () {
            if( $scope.siguienteProveedor > 0){
                $scope.siguienteProveedor = $scope.siguienteProveedor - 5;
                $scope.request('loadPage');
            }
        }

        // Cliente
        $scope.pagination.siguienteCliente = function () {
            $scope.siguienteCliente = $scope.siguienteCliente + 5;
            $scope.request('loadPage');
        }

        $scope.pagination.anteriorCliente = function () {
            if( $scope.siguienteCliente > 0){
                $scope.siguienteCliente = $scope.siguienteCliente - 5;
                $scope.request('loadPage');
            }
        }

        // Producto
        $scope.pagination.siguienteProducto = function () {
            $scope.siguienteProducto = $scope.siguienteProducto + 5;
            $scope.request('loadPage');
        }

        $scope.pagination.anteriorProducto = function () {
            if( $scope.siguienteProducto > 0){
                $scope.siguienteProducto = $scope.siguienteProducto - 5;
                $scope.request('loadPage');
            }
        }

        // Carrito compra
        $scope.pagination.siguienteCarritoCompra = function () {
            $scope.siguienteCarritoCompra = $scope.siguienteCarritoCompra + 5;
            $scope.request('loadPage');
        }

        $scope.pagination.anteriorCarritoCompra = function () {
            if( $scope.siguienteCarritoCompra > 0){
                $scope.siguienteCarritoCompra = $scope.siguienteCarritoCompra - 5;
                $scope.request('loadPage');
            }
        }

        // Carrito venta
        $scope.pagination.siguienteCarritoVenta = function () {
            $scope.siguienteCarritoVenta = $scope.siguienteCarritoVenta + 5;
            $scope.request('loadPage');
        }

        $scope.pagination.anteriorCarritoVenta = function () {
            if( $scope.siguienteCarritoVenta > 0){
                $scope.siguienteCarritoVenta = $scope.siguienteCarritoVenta - 5;
                $scope.request('loadPage');
            }
        }

        // Venta
        $scope.pagination.siguienteVenta = function () {
            $scope.siguienteVenta = $scope.siguienteVenta + 5;
            $scope.request('loadPage');
        }

        $scope.pagination.anteriorVenta = function () {
            if( $scope.siguienteVenta > 0){
                $scope.siguienteVenta = $scope.siguienteVenta - 5;
                $scope.request('loadPage');
            }
        }

        // Compra
        $scope.pagination.siguienteCompra = function () {
            $scope.siguienteCompra = $scope.siguienteCompra + 5;
            $scope.request('loadPage');
        }

        $scope.pagination.anteriorCompra = function () {
            if( $scope.siguienteCompra > 0){
                $scope.siguienteCompra = $scope.siguienteCompra - 5;
                $scope.request('loadPage');
            }
        }

        //Solicitudes de compra
        $scope.pagination.siguienteSolicitud = function () {
            $scope.siguienteSolicitud = $scope.siguienteSolicitud + 5;
            $scope.request('loadPage');
        }

        $scope.pagination.anteriorSolicitud = function () {
            if( $scope.siguienteSolicitud > 0){
                $scope.siguienteSolicitud = $scope.siguienteSolicitud - 5;
                $scope.request('loadPage');
            }
        }

        $scope.actualizarPagina = function(){
            $scope.request('loadPage');
        }

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
                url: 'http://localhost:8080/lazyprime/rest/compraDetalle',
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
                data: {nameProduct: $scope.producto.nameProduct, precioUnitario: $scope.producto.precioUnitario, cantidad:$scope.producto.cantidad}
            }).success(function() {
                $scope.producto.precioUnitario = 0;
                $scope.producto.nameProduct = "";
                $scope.producto.cantidad = 0;
                $scope.producto.product_provider_id = 0;
                $scope.request('loadPage');
            })
        }




        $scope.compraPedido = function(listaDePedidos,provider){
            console.log(provider);
            $http({
                url: 'http://localhost:8080/lazyprime/rest/compras',
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                data: {compraDetalles:listaDePedidos,provider:provider}
            }).success(function(response) {
                $scope.request('loadPage');
            }).error(function (response) {
                alert(response.error);
            })
        }

        $scope.ventaPedido = function(listaDePedidos, cliente){
            $http({
                url: 'http://localhost:8080/lazyprime/rest/ventas',
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                data: {client:cliente,ventaDetalles:listaDePedidos}
            }).success(function(response) {
                $scope.request('loadPage');
            }).error(function (response) {
                alert(response.error);
            })
        }


        // Funciones de eliminacion

        $scope.eliminarProductoDeListaDeCompras = function (id) {
            $http.delete("http://localhost:8080/lazyprime/rest/compraDetalle/" + id).success(function() {
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

        $scope.senFile = function(url){

            var formData = new FormData();
            formData.append("file",$scope.data);

            $http.post(url, formData, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            })
                .success(function( response){
                    console.log("Success");
                    $scope.request("loadPage");
                })
                .error(function(){
                    console.log("Error");
                    $scope.request("loadPage");
                });
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
            $scope.senFile("http://localhost:8080/lazyprime/rest/providers");

        }

        $scope.enviarArchivoProducto = function () {
            $scope.data;
            var f = document.getElementById('fileProducto').files[0],
                r = new FileReader();
            r.onloadend = function(e){
                $scope.data = e.target.result;
            }
            r.readAsBinaryString(f);
            console.log($scope.data);
            $scope.senFile("http://localhost:8080/lazyprime/rest/products");
        }

        $scope.enviarArchivoCompra = function () {
            $scope.data;
            var f = document.getElementById('fileCompra').files[0],
                r = new FileReader();
            r.onloadend = function(e){
                $scope.data = e.target.result;
            }
            r.readAsBinaryString(f);
            console.log($scope.data);
            $scope.senFile("http://localhost:8080/lazyprime/rest/compras");
        }

        $scope.enviarArchivoVenta = function () {
            $scope.data;
            var f = document.getElementById('fileVenta').files[0],
                r = new FileReader();
            r.onloadend = function(e){
                $scope.data = e.target.result;
            }
            r.readAsBinaryString(f);
            console.log($scope.data);
            $scope.senFile("http://localhost:8080/lazyprime/rest/ventas");
        }

        $scope.enviarArchivoCliente = function () {
            $scope.data;
            var f = document.getElementById('fileCliente').files[0],
                r = new FileReader();
            r.onloadend = function(e){
                $scope.data = e.target.result;
            }
            r.readAsBinaryString(f);
            console.log($scope.data);
            $scope.senFile("http://localhost:8080/lazyprime/rest/clients");
        }

        $scope.generarInforme = function(){
            $http.get("http://localhost:8080/lazyprime/rest/reporte",{responseType:'arraybuffer'})
                .success(function(response) {
                    var file = new Blob([response], {type: 'application/pdf'});
                    var fileURL = URL.createObjectURL(file);
                    $scope.informePDF = $sce.trustAsResourceUrl(fileURL);

                }).error(function(response){
                    console.log("Error");
                })
        }

        //Funciones de listado

        //Lista inicial
        $scope.request('loadPage');




    })