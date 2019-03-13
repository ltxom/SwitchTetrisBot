package me.ltxom.tetrisplayer.controller;

import me.ltxom.tetrisplayer.TetrisplayerApplication;
import me.ltxom.tetrisplayer.entity.tetris.Block;
import me.ltxom.tetrisplayer.entity.tetris.PossibleMove;
import me.ltxom.tetrisplayer.entity.tetris.TetrisBoard;
import me.ltxom.tetrisplayer.service.cv.ScreenStreamService;
import me.ltxom.tetrisplayer.service.serial.SerialCommunicationService;
import me.ltxom.tetrisplayer.service.tetris.TetrisBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.LinkedList;

@RestController
public class ApplicationController {
	@Autowired
	private SerialCommunicationService serialCommunicationService;
	@Autowired
	private ScreenStreamService screenStreamService;
	@Autowired
	private TetrisBoardService tetrisBoardService;

	private static String generateCommand(String command) {
		return TetrisplayerApplication.COMMAND_PREFIX + command + TetrisplayerApplication.COMMAND_SUFFIX;
	}

	@RequestMapping(value = "/startgame", method = {RequestMethod.GET})
	public Object start() throws InterruptedException {
		LinkedList<Block> nextQueue = new LinkedList<>();

		new Thread(() -> {
			boolean firstMove = true;
			boolean firstMoveFlag2 = false;
			Block nextBlock = null;
			a:
			for (; ; ) {
				BufferedImage bufferedImage = screenStreamService.captureScreen(new Rectangle(0, 0, 775, 435));

				bufferedImage = ScreenStreamService.imgToGray(bufferedImage);
				TetrisBoard tetrisBoard = tetrisBoardService.analyzeBoardByImg(new Date(), bufferedImage);
				if (tetrisBoard == null || tetrisBoard.getNextBlocks()[0] == null) {
					System.out.println("Game has not started yet");
					break a;
				}

				if (firstMove) {
					for (int i = 0; i < tetrisBoard.getNextBlocks().length; i++) {
						nextQueue.add(tetrisBoard.getNextBlocks()[i]);
					}
				} else {
					if (firstMoveFlag2) {
						nextQueue.add(tetrisBoard.getNextBlocks()[5]);
						firstMoveFlag2 = false;
					}
					for (int i = 0; i < 4; i++) {
						if (!tetrisBoard.getNextBlocks()[i].equals(nextQueue.get(i))) {
							System.out.println(i + ": " + tetrisBoard.getNextBlocks()[i] + "------------" + nextQueue.get(i));
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							continue a;
						}
					}
					nextQueue.add(tetrisBoard.getNextBlocks()[5]);

				}
				if (firstMove)
					nextBlock = nextQueue.remove();
				tetrisBoard.setFallingBlock(nextBlock);
				PossibleMove possibleMove = tetrisBoardService.getNextLocation(tetrisBoard);
				System.out.println(possibleMove.getScore());
				String command = "";

				int lessRight = 0;
//				if (nextBlock instanceof BlockT && (possibleMove.getRotation() == 1 || possibleMove.getRotation() ==
//				3)) {
//					lessRight = 1;
//				}
//				if (nextBlock instanceof BlockT && (possibleMove.getRotation() == 1 || possibleMove.getRotation() ==
//				3)) {
//					lessRight = 1;
//				}

				// 变化
				for (int i = 0; i < possibleMove.getRotation(); i++) {
					command += TetrisplayerApplication.COMMAND_A;
				}
				// 移动到最左边
				for (int i = 0; i < 5; i++) {
					command += TetrisplayerApplication.COMMAND_LEFT;
				}
				for (int i = 0; i < possibleMove.getMove().getDeltaX() - lessRight; i++) {
					command += TetrisplayerApplication.COMMAND_RIGHT;
				}

				command += TetrisplayerApplication.COMMAND_TOP;
				command = generateCommand(command);

				if (firstMove) {
					try {
						Thread.sleep(4000);
						firstMove = false;
						firstMoveFlag2 = true;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
				if (command != null)
					serialCommunicationService.writeString(command);
				nextBlock = nextQueue.remove();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		return "started";
	}
}
