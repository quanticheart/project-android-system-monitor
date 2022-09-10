package com.quanticheart.monitor.project.model

import com.quanticheart.monitor.extentions.UNKNOWN_PARAM

/*
{
  "date": "2022-05-20 16:30:30",
  "connection": {
    "type": "wifi",
    "ip": "0.0.0.0",
    "dns": "0.0.0.0",
    "dns2": "0.0.0.0"
  },
  "screen": "on",
  "battery": {
    "percentage": 100.0,
    "change": false
  },
  "lastLocation": {
    "enable": false,
    "lat": "",
    "long": ""
  },
  "other": {
    "imei1": "",
    "imei2": "",
    "macAddress": ""
  }
}
 */
class SimpleMobileDetails {
    var date: String = ""
    var screen: String = "off"
    var foregraound: Boolean = false

    var app: App = App()
    var battery: Battery = Battery()
    var location: Location = Location()
    var hardware: Hardware = Hardware()
    var connection: Connection = Connection()

    class App {
        var appName: String? = UNKNOWN_PARAM
        var packageName: String? = UNKNOWN_PARAM
        var appVersionCode: Long? = -1
        var appVersionName: String? = UNKNOWN_PARAM
        var targetSdkVersion: Int? = -1
        var minSdkVersion: Int? = -1
        var lastUpdateTime: String? = UNKNOWN_PARAM
        var firstInstallTime: String? = UNKNOWN_PARAM
    }

    class Hardware {
        var brand: String? = UNKNOWN_PARAM
        var device: String? = UNKNOWN_PARAM
        var manufacturer: String? = UNKNOWN_PARAM
        var model: String? = UNKNOWN_PARAM
        var releaseVersion: String? = UNKNOWN_PARAM
        var incremental: String? = UNKNOWN_PARAM
        var sdkInt: Int? = -1
    }

    class Battery {
        var percentage = 0.0
        var charge = UNKNOWN_PARAM
        var plugged = UNKNOWN_PARAM
    }

    class Location {
        var systemCountry = UNKNOWN_PARAM
        var systemLanguage = UNKNOWN_PARAM
        var enable = false
        var permissionsGranted = false
        var latitude = 0.0
        var longitude = 0.0
    }

    class Connection {
        var type: String = UNKNOWN_PARAM
        var speed: String = UNKNOWN_PARAM
        var gatewayIP: String = "0.0.0.0"
        var ipv4: String = "0.0.0.0"
        var ipv6: String = "0.0.0.0"
        var dns1: String = "0.0.0.0"
        var dns2: String = "0.0.0.0"
    }
}