# smeagollib

The working parts of the [Smeagol Wiki engine](https://github.com/journeyman-cc/smeagol), stripped out as a library. It is anticipated that future versions of Smeagol will depend on this library.

## Motivation

One of the significant problems with maintainin the Smeagol Wiki engine is that parts of the HTTP infrastructure on which it depends have changed in ways which it is not trivial to fix. Providing the guts of the engine as a library should make it easier to integrate with different HTTP infrastructure implementations, and also to integrate with other projects.

## Warning

Doesn't work yet.

## Notes

The caller should provide the equivalents of Luminus `handler.clj` and `middleware.clj`.

## License

Copyright © 2014-2020 Simon Brooke. Licensed under the GNU General Public License,
version 2.0 or (at your option) any later version. If you wish to incorporate
parts of Smeagol into another open source project which uses a less restrictive
license, please contact me; I'm open to dual licensing it.
