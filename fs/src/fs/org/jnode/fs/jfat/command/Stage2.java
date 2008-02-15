/*
 * $Id: GrubJFatFormatter.java Tanmoy $
 *
 * JNode.org
 * Copyright (C) 2007 JNode.org
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; If not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.jnode.fs.jfat.command;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.jnode.fs.FileSystemException;
import org.jnode.util.FileUtils;


/**
 * That class installs Grub's Stage 2 on a partition.
 *
 * @author Tango Devian
 */
public class Stage2 {
    private static final Logger log = Logger.getLogger(Stage2.class);
    private static final String GRUB_STAGE_2 = MBRFormatter.GRUB_HOME + "grub.s2";
    private static final String GRUB_MENU_LST = MBRFormatter.GRUB_HOME + "menu.lst";

    /**
     *
     * @param path
     * @throws FileSystemException
     * @throws IOException
     */
    public void format(String path) throws GrubException {
        // writing of the stage2 and menu.LST
        try {
        	System.err.println("path="+path);
            File destDir = new File(path + "/boot/grub/").getAbsoluteFile();
            System.err.println("destDir.parent="+destDir.getParent());
            System.err.println("destDir.name="+destDir.getName());

            if(!destDir.exists()){
                System.out.print("Creating directory: " + destDir.getAbsolutePath() + " ... ");
                destDir.mkdirs();
                System.out.println("done.");
            }

            System.out.print("Writing stage 2 ... ");
            FileUtils.copyFile(GRUB_STAGE_2, destDir.getAbsolutePath(), "stage2");
            System.out.println("done.");

            System.out.print("Writing menu.lst ... ");
            FileUtils.copyFile(GRUB_MENU_LST, destDir.getAbsolutePath(), "menu.lst");
            System.out.println("done.");

        } catch (IOException e) {
            throw new GrubException("error while installing stage 2", e);
        }
    }
}