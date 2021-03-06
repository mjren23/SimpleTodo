# Project 1 - *Simple To Do*

**Simple To Do** is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: **Megan Ren**

Time spent: **8** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **view a list of todo items**
* [x] User can **successfully add and remove items** from the todo list
* [x] User's **list of items persisted** upon modification and and retrieved properly on app restart

The following **optional** features are implemented:

* [x] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list

The following **additional** features are implemented:

* [x] UI/UX improvements
* [x] Support to add completion dates to items
* [ ] Custom adapter to improve style of to do items
* [ ] Ability to reorder items

## Video Walkthrough

Here's a walkthrough of implemented user stories:

app with date support added:

<img src='walkthrough3.gif' title='Updated Walkthrough' width='' alt='Video Walkthrough of app' />

updated app: 

<img src='updatedwalkthrough.gif' title='Video Walkthrough' width='' alt='Video Walkthrough of app' />

original app:

<img src='walkthrough.gif' title='Video Walkthrough' width='' alt='Video Walkthrough of app' />

GIFs created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Needed to import <code>org.apache.commons.io.FileUtils</code> instead of <code>android.os.FileUtils</code> while implementing persistence. 
Issues with forcing soft input (keyboard) to open on certain actions.

## License

    Copyright [2020] [Megan Ren]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
