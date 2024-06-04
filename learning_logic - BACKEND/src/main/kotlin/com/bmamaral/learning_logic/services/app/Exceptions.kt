package com.bmamaral.learning_logic.services.app

class NotFoundException(s: String) : RuntimeException(s)

class PreconditionFailedException(s: String) : RuntimeException(s)

class InvalidSymbolException(s: String) : RuntimeException(s)

class UnknownExerciseTypeException(s: String) : RuntimeException(s)

