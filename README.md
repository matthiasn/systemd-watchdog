# systemd-watchdog - Notify systemd's watchdog

## Usage

Import the library:

    (:require [matthiasn.systemd-watchdog.core :as wd])

Start the watchdog notifier:

    (wd/start-watchdog! 5000)

That's it.


## License

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
