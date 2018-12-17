# LodInspector

This is a simple hack developed at the BioHackathon 2016. Given a search string, it queries YummyData (yummydata.org) for a list of active endpoints, then:
- searches for all entities that have some property with object this string.
- find out what kind of predicates are used
- re-queries all active endpoints for all statements about those entities
- does a very rudimentary overlap analysis

It's crude and done in an hurry ;) Also text-search/overlap is extremely rude. It's a hackathon, let's say it's the idea that matters.

At the 2018 Hackathon, some more work was done to provide some profiling and and visual interface (overlap analsyis and the re-querying of datasets for expanded labels have been temporarily removed).
