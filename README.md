# systemd-watchdog

## Running Clojure app under systemd

How do you deploy your Clojure applications, and how do you keep them running?
In most recent and relevant Linux
distributions, **[systemd](https://en.wikipedia.org/wiki/Systemd)**
is the default init system, so the easiest thing to do is create a systemd
service to start your application as a service. You can find examples of that
for the services running on my box hosting **[matthiasnehlsen.com](http://matthiasnehlsen.com/)**
and all of the related live examples in this **[repo](https://github.com/matthiasn/conf)**.

[![Dependencies Status](https://jarkeeper.com/matthiasn/systemd-watchdog/status.svg)](https://jarkeeper.com/matthiasn/systemd-watchdog)

## Supervision by systemd

With systemd, there's the concept of the watchdog. An application needs to issue
`sd_notify` commands every so often, otherwise it's killed and restarted.
For more on the subject, I recommend this
[blog post](http://0pointer.de/blog/projects/watchdog.html) by **Lennart Poettering**,
one of systemd's original authors.

This library facilitates the required calls to systemd via **[JNA](https://github.com/java-native-access/jna)**,
using the **[SDNotify](https://github.com/faljse/SDNotify)** library, but only if
the `NOTIFY_SOCKET` environment variable is set. Otherwise, nothing happens, thus
there's no harm even if you're on an entirely different platform during development.


## Usage

Import the library:

    (:require [matthiasn.systemd-watchdog.core :as wd])

Start the watchdog notifier:

    (wd/start-watchdog! 5000)

That's it.


## Examples

You can have a look at my live examples, these are all supervised by systemd now.

* **[BirdWatch MainApp](https://github.com/matthiasn/BirdWatch/blob/master/Clojure-Websockets/MainApp/src/clj/birdwatch/main.clj)**
* **[BirdWatch twitterClient](https://github.com/matthiasn/BirdWatch/blob/master/Clojure-Websockets/TwitterClient/src/clj/birdwatch_tc/main.clj)**
* **[systems-toolbox redux example](https://github.com/matthiasn/systems-toolbox/blob/master/examples/redux-counter01/src/clj/example/core.clj)**
* **[systems-toolbox ws example](https://github.com/matthiasn/systems-toolbox/blob/master/examples/trailing-mouse-pointer/src/clj/example/core.clj)**


The service files for all these projects are in the **[conf](https://github.com/matthiasn/conf)**
repo, have a look at the watchdog configuration there, for example in
`birdwatch.service` or in `systems-toolbox-ws-latency.service`.


## REPL-driven development

You can call the `start-watchdog!` function in any kind of REPL-driven workflow
as often as you want, it's not going to do anything unless the `NOTIFY_SOCKET`
environment variable is set.


## Prior artwork

The most important building block here is
the **[SDNotify](https://github.com/faljse/SDNotify)** Java library. This
library is then called inside a
minimal **[systems-toolbox](https://github.com/matthiasn/systems-toolbox)**
system, which can be embedded in any **Clojure** application.


## License

Distributed under the Eclipse Public License either version 1.0 or
(at your option) any later version.
