// @dart=2.18
//
// Совместимый с web шим вместо символов dart:io, которые тянет
// сгенерированный dart-клиент.
//
part of openapi.api;

class HttpStatus {
  static const int noContent = 204;
  static const int badRequest = 400;
  static const int internalServerError = 500;
}

class SocketException implements Exception {
  final String message;
  SocketException(this.message);
  @override
  String toString() => 'SocketException: $message';
}

class TlsException implements Exception {
  final String message;
  TlsException(this.message);
  @override
  String toString() => 'TlsException: $message';
}

class IOException implements Exception {
  final String message;
  IOException(this.message);
  @override
  String toString() => 'IOException: $message';
}
