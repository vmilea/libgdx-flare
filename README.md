## libgdx-flare: FLexible Action REplacement for libGDX

Alternative actions library for libGDX. It's reasonably complete but don't expect documentation.

Extra features:

* Supports animation of used defined properties
* Composable tween (interpolation) actions, which can be winded back and forth
* Lazy actions. Can insert conditional actions within a sequence and other dirty tricks.
* Animator class to help manage action groups
* Better numerical stability. Can do `repeatForever(rotateBy(360))` without glitches.

See [Actions.java](src/com/vmilea/gdx/flare/Actions.java) for the works.
