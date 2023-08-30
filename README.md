# Compatibility Update
Unfortunately, starting with Fritz!OS 7.50, this program does no longer work on repeaters, powerline etc products from AVM. This is due to internal redesigns, which removed the certificate upload support from the webinterface (the UI still supports HTTPS and uses the last configured certificate, but you cannot update it anymore). Until AVM re-adds this functionality (it currently unknown if or when this happens), this tool will not work anymore. It still works on FritzBox builds, but not the "lite" builds used for non-router devices.

# FritzCerts
Ever noticed that your Fritz!Box supports https? It does so out of the box (pun intended), but only with a self-signed certificate. Browsers do not trust this self-signed certificate, unless the user creates a specific exception. Users blindly proceeding are vulnerable to MITM attacks. To make it even worse, AVM products regenerate the cert everytime it's IP addresses changes - which can be daily. This makes it impossible to add sensible trust exceptions to the self-signed certificate.

# Solution provided by AVM
AVM has seen this issue and offered a solution, which is based on retrieving certificates from Let's Encrypt. However, this requires your Box to be accessible from the internet and also requires a MyFritz account. Additionally, this does not work for internal devices (repeaters, powerline, additional routers, etc) which are not reachable from the internet.

# Solution provided by this program
If the solution by AVM is not for you, then maybe this software is. This software allows you to upload your own certificates to your AVM product. You could, for example, create your own local certificate authority which is trusted in your LAN. If you upload CA-signed certificates to your AVM products, you will get fully working https to these devices.

This program does not aid in creating these certificates - thus, this software is only suitable for professional users that can issue and manage web-certificates.

# Compatibility and technical notes
This program utilizes functionality available in the webinterface of Fritz!OS. Versions 7 and higher have this feature accessible under "Internet -> releases -> Fritz!Box services -> certificate" (note: translated from german version, GUI menu may differ in international version). At this menu point, any user can upload custom (RSA) certificates. The TLS implementation of Fritz!OS at this point only supports *RSA* certificates. Other keytypes, like ECDSA, are currently unsupported.

### Wait: If there's already GUI functionality that does this, what is this program for?
-> That's the point. Only Fritz!Box products have this menu. Repeaters, powerline, etc products from AVM do *not* have this menu. However, the *functionality is just hidden, it is indeed present in almost all devices running Fritz!OS with a web gui*.
This software works with these products, even though the menu is hidden. Thus, an external program is the only option to access the feature. In conclusion, this software works with most *Fritz!Box, Fritz!WLAN and Fritz!Powerline products* (tested only with devices running Fritz!OS 7 and higher).

# Compatibility and technical notes (continued)
Fritz!OS remembers if a custom certificate has been uploaded and disables the (re-)generation of self-signed certificates if one is present. Fritz!OS supports password protected .pem files, but this tool supports only unprotected pem files at the moment. Both certificate and private key must be in the same file, encoded in standard PEM format with header-guards (=== BEGIN ... === etc).

# Usage
`java -jar <jarfile> <filepath> <domain> [username] <password>`

`<filepath>` is the name and path to a .pem file, which contains public & private key (full certificate with private key). Path can be relative.

`<domain>` can be a hostname or an IP address, pointing to an AVM product. It's also possible to specify an entire URL like http://fritz.repeater/. HTTPS will only work if a valid certificate is used (non-default).

`[username]` is optional, the default configuration only uses a password.

`<password>` is the password of the webinterface. If not set, any given here will be valid.


# Dependencies
There's a Maven file (pom.xml) included, which should automatically manage dependencies. 

If you do not use Maven:
FritzCerts requires [Apache HttpClients](https://hc.apache.org/httpcomponents-client-ga/index.html) as well as [Apache HttpMime](https://hc.apache.org/httpcomponents-client-ga/httpmime/summary.html) in a recent version and Java 7 or higher to compile.

# Download
You can download an up to date pre-compiled version of this program here (Compiled with Java 11):
https://build.germancoding.com/job/FritzCerts/lastSuccessfulBuild/artifact/target/FritzCerts-0.0.1-SNAPSHOT-jar-with-dependencies.jar

# Additional notes
The program is not fully finished at the moment. There's no pretty status printing yet, instead full HTML pages are printed to the console. Additionally, password protected files should be supported and some other improvements are planed too.

This software copies code from [FritzLED](https://github.com/GermanCoding/FritzLED).

Neither this program nor the author(s) are affiliated with AVM. The name of this program may change in the future.
