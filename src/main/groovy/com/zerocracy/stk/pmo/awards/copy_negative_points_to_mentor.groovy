/*
 * Copyright (c) 2016-2019 Zerocracy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to read
 * the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.zerocracy.stk.pmo.awards

import com.jcabi.xml.XML
import com.zerocracy.Farm
import com.zerocracy.Par
import com.zerocracy.Project
import com.zerocracy.entry.ClaimsOf
import com.zerocracy.farm.Assume
import com.zerocracy.claims.ClaimIn
import com.zerocracy.pmo.People

def exec(Project project, XML xml) {
  new Assume(project, xml).notPmo()
  new Assume(project, xml).type('Add award points')
  ClaimIn claim = new ClaimIn(xml)
  String login = claim.param('login')
  int minutes = Integer.parseInt(claim.param('minutes'))
  if (minutes >= 0) {
    return
  }
  Farm farm = binding.variables.farm
  People people = new People(farm).bootstrap()
  if (!people.hasMentor(login)) {
    return
  }
  if (people.mentor(login) == '0crat') {
    return
  }
  if (claim.hasParam('student')) {
    return
  }
  String mentor = people.mentor(login)
  claim.copy()
    .param('login', mentor)
    .param('student', login)
    .param(
      'reason',
      new Par('Mistake of @%s (your student): ').say(login) +
        claim.param('reason')
    )
    .postTo(new ClaimsOf(farm, project))
}
