#!/usr/bin/env bash
mkdir -p ~/.m2
base64 -d <<EOF > ~/.m2/settings.xml
PHNldHRpbmdzIHhtbG5zPSJodHRwOi8vbWF2ZW4uYXBhY2hlLm9yZy9TRVRUSU5HUy8xLjAuMCIK
ICB4bWxuczp4c2k9Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvWE1MU2NoZW1hLWluc3RhbmNlIgog
IHhzaTpzY2hlbWFMb2NhdGlvbj0iaHR0cDovL21hdmVuLmFwYWNoZS5vcmcvU0VUVElOR1MvMS4w
LjAKICAgICAgICAgICAgICAgICAgICAgIGh0dHA6Ly9tYXZlbi5hcGFjaGUub3JnL3hzZC9zZXR0
aW5ncy0xLjAuMC54c2QiPgoKICA8YWN0aXZlUHJvZmlsZXM+CiAgICA8YWN0aXZlUHJvZmlsZT5n
aXRodWI8L2FjdGl2ZVByb2ZpbGU+CiAgPC9hY3RpdmVQcm9maWxlcz4KCiAgPHByb2ZpbGVzPgog
ICAgPHByb2ZpbGU+CiAgICAgIDxpZD5naXRodWI8L2lkPgogICAgICA8cmVwb3NpdG9yaWVzPgog
ICAgICAgIDxyZXBvc2l0b3J5PgogICAgICAgICAgPGlkPmNlbnRyYWw8L2lkPgogICAgICAgICAg
PHVybD5odHRwczovL3JlcG8xLm1hdmVuLm9yZy9tYXZlbjIvPC91cmw+CiAgICAgICAgPC9yZXBv
c2l0b3J5PgogICAgICAgIDxyZXBvc2l0b3J5PgogICAgICAgICAgPGlkPmdpdGh1YjwvaWQ+CiAg
ICAgICAgICA8dXJsPmh0dHBzOi8vbWF2ZW4ucGtnLmdpdGh1Yi5jb20va2VlcHMvKi88L3VybD4K
ICAgICAgICAgIDxzbmFwc2hvdHM+CiAgICAgICAgICAgIDxlbmFibGVkPnRydWU8L2VuYWJsZWQ+
CiAgICAgICAgICA8L3NuYXBzaG90cz4KICAgICAgICA8L3JlcG9zaXRvcnk+CiAgICAgIDwvcmVw
b3NpdG9yaWVzPgogICAgPC9wcm9maWxlPgogIDwvcHJvZmlsZXM+CgogIDxzZXJ2ZXJzPgogICAg
PHNlcnZlcj4KICAgICAgPGlkPmdpdGh1YjwvaWQ+CiAgICAgIDx1c2VybmFtZT5sdWlzMTAwPC91
c2VybmFtZT4KICAgICAgPHBhc3N3b3JkPmdocF9reVMxbDBJZmM4S0xoSDh0SDAwQ3JSaEREMlgz
OHYxRDgycEU8L3Bhc3N3b3JkPgogICAgPC9zZXJ2ZXI+CiAgPC9zZXJ2ZXJzPgo8L3NldHRpbmdz
Pgo=
EOF