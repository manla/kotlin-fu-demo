package com.maranin.kotlinfundemo.shared

import java.lang.RuntimeException

class InvalidEntryException(msg: String): RuntimeException(msg)

class NoEntryAvailableException(msg: String): RuntimeException(msg)