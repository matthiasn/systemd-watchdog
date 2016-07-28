(ns matthiasn.systemd-watchdog.core
  (:require [matthiasn.systems-toolbox.switchboard :as sb]
            [matthiasn.systems-toolbox.scheduler :as sched])
  (:import [info.faljse.SDNotify SDNotify]))

(defonce switchboard (sb/component :wd/switchboard))
(defn start-watchdog!
  "Call systemd's watchdog every so many milliseconds."
  [timeout]
  (when (get (System/getenv) "NOTIFY_SOCKET")
    (sb/send-mult-cmd
      switchboard
      [[:cmd/init-comp (sched/cmp-map :wd/scheduler-cmp)]
       [:cmd/init-comp
        {:cmp-id      :wd/notify-cmp
         :handler-map {:wd/send (fn [_] (SDNotify/sendWatchdog))}}]
       [:cmd/send {:to  :wd/scheduler-cmp
                   :msg [:cmd/schedule-new {:timeout timeout
                                            :message [:wd/send]
                                            :repeat  true}]}]
       [:cmd/route {:from :wd/scheduler-cmp :to :wd/notify-cmp}]])))
