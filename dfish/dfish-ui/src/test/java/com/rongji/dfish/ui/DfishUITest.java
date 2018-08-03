package com.rongji.dfish.ui;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.rongji.dfish.ui.helper.DfishUIHelperTest;
import com.rongji.dfish.ui.layout.DfishUILayoutTest;

@RunWith(Suite.class)
@SuiteClasses({DfishUIHelperTest.class,DfishUILayoutTest.class})
public class DfishUITest {

}

