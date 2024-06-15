# unifi-rebooter

A tiny Java app that SSHs into UniFi Access Points and other Ubiquiti devices and auto-reboots them on a cron schedule.

Because, yeah.

## Why

My [UniFi In-Wall HD Access Points](https://store.ui.com/collections/unifi-network-access-points/products/unifi-in-wall-hd) were randomly falling offline and disconnecting themselves from my [UniFi UDM](https://store.ui.com/products/unifi-dream-machine) (controller) every week or so. Like clockwork, every 5-7 days, I'd find two or three of my In-Wall HDs offline and "Unable to Add" according to my UDM. The only way out was a factory reset of the APs and a forced re-adoption.

In attempt to alleviate this problem, I wrote this tiny app and have it running on a Raspberry Pi 4B in my home data closet. As configured, every Tuesday, Thursday, and Saturday night my `unifi-rebooter` does a rolling reboot of every In-Wall HD AP on my home network.

As suspected, a regular reboot of the UniFi APs in my home has made things better &mdash; none of my IW-HDs have fallen into this forsaken state with a regular reboot.

I shouldn't have to do this.

## Building

This project is built for Java 11, but may work just fine on other versions. YMMV.

Clone or fork the repository.

    git clone https://github.com/markkolich/unifi-rebooter.git

To package a runnable uber JAR, run `mvn package`:

    mvn package

The resulting JAR is placed into the **dist** directory.

## Setup

Create a fresh [lightbend/config](https://github.com/lightbend/config) configuration file and place it somewhere secure, on a trusted device. Your `unifi-rebooter.conf` config file should look something like this:

```
include "application"

unifi-rebooter {

  devices = [
    {
      hostname = "my.device-1.local"
      username = "admin"
      password = "[SSH password here]"
      cronExpression = "0 0 0 ? * TUE,THU,SAT *"
    },
    {
      hostname = "my.device-2.local"
      username = "admin"
      password = "[SSH password here]"
      cronExpression = "0 30 0 ? * TUE,THU,SAT *"
    },
    {
      hostname = "my.device-3.local"
      username = "admin"
      password = "[SSH password here]"
      cronExpression = "0 0 1 ? * TUE,THU,SAT *"
    }
  ]

}
```

Note, the `cronExpression` configuration directive must be a [Quartz compatible](http://www.quartz-scheduler.org/documentation/quartz-2.3.0/tutorials/crontrigger.html) cron expression.

## Running

Run the app by invoking:

    java -Dconfig.file=/path/to/your/unifi-rebooter.conf -jar dist/unifi-rebooter-0.1-runnable.jar

Once running, hit the tiny web-server at http://localhost:8080/unifi-rebooter to check for status.

Leave it running and watch your devices reboot as you have configured!

## Licensing

Copyright (c) 2024 <a href="https://mark.koli.ch">Mark S. Kolich</a>.

All code in this project is freely available for use and redistribution under the <a href="http://opensource.org/comment/991">MIT License</a>.

See <a href="https://github.com/markkolich/unifi-rebooter/blob/master/LICENSE">LICENSE</a> for details.
