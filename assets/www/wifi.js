/* Copyright (c) 2011 - Andago
*
* author: Daniel Tizón
*
* Licensed under the Apache License, Version 2.0 (the "License"); you may not
* use this file except in compliance with the License. You may obtain a copy of
* the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations under
* the License.
*/


var Wifi = function() {
};




/**
* @param argument Argument that we are going to pass to the plugin - for this method no arguments are needed
* @param successCallback The callback which will be called when listDevices is successful
* @param failureCallback The callback which will be called when listDevices encouters an error
*/
Wifi.prototype.Scan = function(argument,successCallback, failureCallback) {

    return PhoneGap.exec(successCallback, failureCallback, 'WifiPlugin', 'Scan', [argument]);
};


PhoneGap.addConstructor(function() {
PhoneGap.addPlugin("wifi", new Wifi());
});