(ns matthiasn.systemd-watchdog.core
  (:require [matthiasn.systems-toolbox.switchboard :as sb]
            [matthiasn.systems-toolbox.scheduler :as sched])
  (:import [info.faljse.SDNotify SDNotify]))

(defn start-watchdog!
  "Call systemd's watchdog every so many milliseconds.
   Requires the NOTIFY_SOCKET environment variable to be set, otherwise does
   nothing. Fires up a minimal systems-toolbox system with two components:
    * a scheduler component
    * a component notifying systemd.
   Then, the scheduler will emit messages every so often, and upon receiving,
   the notifying component will call the sendWatchdog function.
   Takes the timeout in milliseconds."
  [timeout]
  (when (get (System/getenv) "NOTIFY_SOCKET")
    (sb/send-mult-cmd
      (sb/component :wd/switchboard)
      [[:cmd/init-comp (sched/cmp-map :wd/scheduler-cmp)]
       [:cmd/init-comp
        {:cmp-id      :wd/notify-cmp
         :handler-map {:wd/send (fn [_] (SDNotify/sendWatchdog))}}]
       [:cmd/send {:to  :wd/scheduler-cmp
                   :msg [:cmd/schedule-new
                         {:timeout timeout
                          :message [:wd/send]
                          :repeat  true}]}]
       [:cmd/route {:from :wd/scheduler-cmp :to :wd/notify-cmp}]])))
