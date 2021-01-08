package com.maranin.kotlinfundemo.v4io

import java.lang.RuntimeException

class InvalidEntryException(msg: String): RuntimeException(msg)

class NoEntryAvailableException(msg: String): RuntimeException(msg)