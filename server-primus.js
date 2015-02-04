// var Primus = require('primus')
//   , http = require('http')

// var server = http.createServer(function (req, res) {
//   res.end('nothing here\n')
// }).listen(8080)

// var primus = new Primus(server)

// primus.on('connection', function (spark) {
//   spark.on('data', function (data) {
//     console.log(data)
//   })
// })

var Primus = require('primus')
  , http = require('http')
  , Rooms = require('primus-rooms')

var server = http.createServer(function (req, res) {
  res.end('nothing here\n')
}).listen(8000)

var primus = new Primus(server)
primus.use('rooms', Rooms)

primus.on('connection', function (spark) {
  spark.join('abc123', function () {
    spark.on('data', function (data) {
      console.log('abc123', data)
    })
  })

  spark.join('abc456', function () {
    spark.on('data', function (data) {
      console.log('abc456', data)
    })
  })
})