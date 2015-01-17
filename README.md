## libgdx-flare: FLexible Action REplacement for libGDX

Alternative actions library for libGDX. It's reasonably complete but don't expect documentation.

Extra features:

* Supports animation of user defined properties.
* Composable tween (interpolation) actions, which can be winded back and forth
* Lazy actions. Can inject conditional actions within a sequence and other dirty tricks.
* Predicate actions. Can delay a sequence until arbitrary events like user input or file I/O, or for coordination with other actions.
* Animator class to help manage action groups
* Smoother animation. When an action ends, unconsumed delta time is carried over to the next action in the sequence.

See [Actions.java](src/com/vmilea/gdx/flare/Actions.java) for the works.
