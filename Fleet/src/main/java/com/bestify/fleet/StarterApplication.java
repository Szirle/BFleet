/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.bestify.fleet;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;


public class StarterApplication extends Application {
//  Password For ParseDashbord:  UZb6jinbLVMw

  @Override
  public void onCreate() {
    super.onCreate();

    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);

    // Add your initialization code here
    Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
            .applicationId("e9f432cba30506ce0a92b8b44f6ea1e114289b48")
            .clientKey("f08f3f042e4e7e0ad44a70fc629f3083db0ac63d")
            .server("http://18.218.165.13:80/parse/")
            .build()
    );



    // ParseUser.enableAutomaticUser();

    ParseACL defaultACL = new ParseACL();
    defaultACL.setPublicReadAccess(true);
    defaultACL.setPublicWriteAccess(true);
    ParseACL.setDefaultACL(defaultACL, true);

  }
}
